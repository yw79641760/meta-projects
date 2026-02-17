package com.softmegatron.shared.meta.validation.exception;

import com.softmegatron.shared.meta.validation.model.Violation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * ViolationException 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class ViolationExceptionTest {

    private static final String TEST_MESSAGE = "Test violation message";
    private static final String TEST_PROPERTY_PATH = "fieldName";
    private static final Object TEST_TARGET = new Object();
    private static final Object TEST_INVALID_VALUE = "invalid";

    private Set<Violation> testViolations;

    @Before
    public void setUp() {
        testViolations = new HashSet<>();
        testViolations.add(new Violation(TEST_MESSAGE, TEST_TARGET, TEST_PROPERTY_PATH, TEST_INVALID_VALUE));
    }

    @Test
    public void testConstructorWithViolations() {
        ViolationException exception = new ViolationException(testViolations);
        assertNotNull(exception);
        assertEquals(testViolations, exception.getViolations());
    }

    @Test
    public void testGetViolations() {
        ViolationException exception = new ViolationException(testViolations);
        Set<Violation> violations = exception.getViolations();
        assertNotNull(violations);
        assertEquals(testViolations, violations);
    }

    @Test
    public void testGetMessage() {
        ViolationException exception = new ViolationException(testViolations);
        String message = exception.getMessage();
        assertNotNull(message);
        assertTrue(message.contains(TEST_MESSAGE));
        assertTrue(message.contains(TEST_PROPERTY_PATH));
        assertTrue(message.contains(TEST_INVALID_VALUE.toString()));
    }

    @Test
    public void testGetMessageWithMultipleViolations() {
        testViolations.add(new Violation("Second message", TEST_TARGET, "field2", 123));
        testViolations.add(new Violation("Third message", TEST_TARGET, "field3", "test"));

        ViolationException exception = new ViolationException(testViolations);
        String message = exception.getMessage();

        assertNotNull(message);
        assertTrue(message.contains(TEST_MESSAGE));
        assertTrue(message.contains("Second message"));
        assertTrue(message.contains("Third message"));
    }

    @Test
    public void testGetMessageWithEmptyViolations() {
        Set<Violation> emptyViolations = new HashSet<>();
        ViolationException exception = new ViolationException(emptyViolations);
        String message = exception.getMessage();
        assertNotNull(message);
        assertEquals("", message);
    }

    @Test
    public void testBuildFromEmptyConstraintViolations() {
        ViolationException exception = ViolationException.build(Collections.emptySet());
        assertNotNull(exception);
        assertTrue(exception.getViolations().isEmpty());
    }

    @Test
    public void testBuildFromNullConstraintViolations() {
        ViolationException exception = ViolationException.build(null);
        assertNotNull(exception);
        assertTrue(exception.getViolations().isEmpty());
    }

    @Test
    public void testBuildFromActualConstraintViolations() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestBean bean = new TestBean(null);
        Set<ConstraintViolation<TestBean>> cvs = validator.validate(bean);

        ViolationException exception = ViolationException.build(cvs);
        assertNotNull(exception);
        assertFalse(exception.getViolations().isEmpty());

        Violation violation = exception.getViolations().iterator().next();
        assertNotNull(violation.getMessage());
        assertEquals("name", violation.getPropertyPath());
        assertNull(violation.getInvalidValue());
    }

    @Test
    public void testBuildFromRegexAndValue() {
        String regex = "\\d+";
        String value = "abc";

        ViolationException exception = ViolationException.build(regex, value);
        assertNotNull(exception);
        assertEquals(1, exception.getViolations().size());

        Violation violation = exception.getViolations().iterator().next();
        assertTrue(violation.getMessage().contains("not matching"));
        assertTrue(violation.getMessage().contains(value));
        assertTrue(violation.getMessage().contains(regex));
        assertEquals(regex, violation.getInvalidValue());
        assertNull(violation.getPropertyPath());
        assertEquals(value, violation.getTarget());
    }

    @Test
    public void testBuildFromRegexWithMatchingValue() {
        ViolationException exception = ViolationException.build("\\d+", "123");
        assertNotNull(exception);
        assertEquals(1, exception.getViolations().size());
    }

    @Test
    public void testExtendsRuntimeException() {
        ViolationException exception = new ViolationException(testViolations);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void testCanThrowAndCatch() {
        ViolationException exception = new ViolationException(testViolations);
        try {
            throw exception;
        } catch (ViolationException e) {
            assertEquals(exception, e);
            assertEquals(testViolations, e.getViolations());
        }
    }

    @Test
    public void testCanCatchAsRuntimeException() {
        ViolationException exception = new ViolationException(testViolations);
        try {
            throw exception;
        } catch (RuntimeException e) {
            assertTrue(e instanceof ViolationException);
            assertEquals(exception, e);
        }
    }

    @Test
    public void testMessageFormatContainsAllInfo() {
        Violation violation = new Violation("must not be null", "TestBean[name=null]", "name", null);
        Set<Violation> violations = new HashSet<>();
        violations.add(violation);

        ViolationException exception = new ViolationException(violations);
        String message = exception.getMessage();

        assertTrue(message.contains("must not be null"));
        assertTrue(message.contains("name"));
        assertTrue(message.contains("null"));
    }

    @Test
    public void testMessageWithNullValues() {
        Violation violation = new Violation(null, null, null, null);
        Set<Violation> violations = new HashSet<>();
        violations.add(violation);

        ViolationException exception = new ViolationException(violations);
        String message = exception.getMessage();
        assertNotNull(message);
    }

    @Test
    public void testBuildPreservesTarget() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestBean bean = new TestBean(null);
        Set<ConstraintViolation<TestBean>> cvs = validator.validate(bean);

        ViolationException exception = ViolationException.build(cvs);
        Violation violation = exception.getViolations().iterator().next();

        assertNotNull(violation.getTarget());
        assertNotNull(violation.getPropertyPath());
        assertEquals("name", violation.getPropertyPath());
    }

    @Test
    public void testGetViolationsReturnsOriginalSet() {
        ViolationException exception = new ViolationException(testViolations);
        assertSame(testViolations, exception.getViolations());
    }

    @Test
    public void testHasSerialVersionUID() throws NoSuchFieldException {
        var field = ViolationException.class.getDeclaredField("serialVersionUID");
        assertTrue(java.lang.reflect.Modifier.isStatic(field.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()));
        assertEquals(long.class, field.getType());
    }

    private static class TestBean {
        @NotNull
        private String name;

        public TestBean(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestBean[name=" + name + "]";
        }
    }
}
