package com.softmegatron.shared.meta.validation.exception;

import com.softmegatron.shared.meta.validation.model.Violation;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ViolationException 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ViolationException 异常类
 * @date 2026/2/6 16:20
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
        
        assertNotNull("异常不应该为null", exception);
        assertEquals("violations应该正确", testViolations, exception.getViolations());
    }

    @Test
    public void testGetViolations() {
        ViolationException exception = new ViolationException(testViolations);
        Set<Violation> violations = exception.getViolations();
        
        assertNotNull("violations不应该为null", violations);
        assertEquals("violations应该正确", testViolations, violations);
    }

    @Test
    public void testGetMessage() {
        ViolationException exception = new ViolationException(testViolations);
        String message = exception.getMessage();
        
        assertNotNull("message不应该为null", message);
        assertTrue("message应该包含violation message", message.contains(TEST_MESSAGE));
        assertTrue("message应该包含propertyPath", message.contains(TEST_PROPERTY_PATH));
        assertTrue("message应该包含invalidValue", message.contains(TEST_INVALID_VALUE.toString()));
        System.out.println("Exception message: " + message);
    }

    @Test
    public void testGetMessageWithMultipleViolations() {
        testViolations.add(new Violation("Second message", TEST_TARGET, "field2", 123));
        testViolations.add(new Violation("Third message", TEST_TARGET, "field3", "test"));
        
        ViolationException exception = new ViolationException(testViolations);
        String message = exception.getMessage();
        
        assertNotNull("message不应该为null", message);
        assertTrue("message应该包含第一个violation", message.contains(TEST_MESSAGE));
        assertTrue("message应该包含第二个violation", message.contains("Second message"));
        assertTrue("message应该包含第三个violation", message.contains("Third message"));
        System.out.println("Multiple violations message: " + message);
    }

    @Test
    public void testGetMessageWithEmptyViolations() {
        Set<Violation> emptyViolations = new HashSet<>();
        ViolationException exception = new ViolationException(emptyViolations);
        String message = exception.getMessage();
        
        assertNotNull("message不应该为null", message);
        assertEquals("message应该是空字符串", "", message);
    }

    @Test
    public void testBuildFromConstraintViolations() {
        Set<Violation> violations = ViolationException.build(Collections.emptySet()).getViolations();
        
        assertNotNull("violations不应该为null", violations);
        assertTrue("violations应该是空的", violations.isEmpty());
    }

    @Test
    public void testBuildFromNullConstraintViolations() {
        Set<Violation> violations = ViolationException.build(null).getViolations();
        
        assertNotNull("violations不应该为null", violations);
        assertTrue("violations应该是空的", violations.isEmpty());
    }

    @Test
    public void testBuildFromRegexAndValue() {
        String regex = "\\d+";
        String value = "abc";
        
        ViolationException exception = ViolationException.build(regex, value);
        Set<Violation> violations = exception.getViolations();
        
        assertNotNull("异常不应该为null", exception);
        assertNotNull("violations不应该为null", violations);
        assertEquals("violations应该有1个元素", 1, violations.size());
        
        Violation violation = violations.iterator().next();
        assertNotNull("violation不应该为null", violation);
        assertTrue("message应该包含'not matching'", 
                  violation.getMessage().contains("not matching"));
        assertTrue("message应该包含value", violation.getMessage().contains(value));
        assertTrue("message应该包含regex", violation.getMessage().contains(regex));
        assertEquals("invalidValue应该是regex", regex, violation.getInvalidValue());
        assertEquals("propertyPath应该为null", null, violation.getPropertyPath());
    }

    @Test
    public void testBuildFromRegexAndMatchingValue() {
        String regex = "\\d+";
        String value = "12345";
        
        ViolationException exception = ViolationException.build(regex, value);
        Set<Violation> violations = exception.getViolations();
        
        assertNotNull("异常不应该为null", exception);
        assertNotNull("violations不应该为null", violations);
        assertEquals("violations应该有1个元素", 1, violations.size());
        
        Violation violation = violations.iterator().next();
        assertNotNull("violation不应该为null", violation);
        assertTrue("message应该包含value", violation.getMessage().contains(value));
        assertEquals("invalidValue应该是regex", regex, violation.getInvalidValue());
        assertEquals("propertyPath应该为null", null, violation.getPropertyPath());
    }

    @Test
    public void testExtendsRuntimeException() {
        ViolationException exception = new ViolationException(testViolations);
        assertTrue("ViolationException应该继承RuntimeException", exception instanceof RuntimeException);
    }

    @Test
    public void testHasSerialVersionUID() throws NoSuchFieldException {
        var serialVersionUidField = ViolationException.class.getDeclaredField("serialVersionUID");
        assertNotNull("serialVersionUID字段应该存在", serialVersionUidField);
        assertTrue("serialVersionUID应该是long类型", long.class == serialVersionUidField.getType());
        assertTrue("serialVersionUID应该是final", 
                  java.lang.reflect.Modifier.isFinal(serialVersionUidField.getModifiers()));
        assertTrue("serialVersionUID应该是static", 
                  java.lang.reflect.Modifier.isStatic(serialVersionUidField.getModifiers()));
    }

    @Test
    public void testIsPublicClass() {
        int modifiers = ViolationException.class.getModifiers();
        assertTrue("ViolationException应该是public类", 
                  java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    public void testViolationsFieldIsPrivate() throws NoSuchFieldException {
        var violationsField = ViolationException.class.getDeclaredField("violations");
        assertNotNull("violations字段应该存在", violationsField);
        assertTrue("violations应该是private", 
                  java.lang.reflect.Modifier.isPrivate(violationsField.getModifiers()));
    }

    @Test
    public void testBuildMethodIsStatic() throws NoSuchMethodException {
        var buildMethod = ViolationException.class.getMethod("build", java.util.Set.class);
        assertNotNull("build方法应该存在", buildMethod);
        assertTrue("build方法应该是static", 
                  java.lang.reflect.Modifier.isStatic(buildMethod.getModifiers()));
    }

    @Test
    public void testBuildMethodReturnsViolationException() throws NoSuchMethodException {
        var buildMethod = ViolationException.class.getMethod("build", java.util.Set.class);
        assertTrue("build方法应该返回ViolationException", 
                  ViolationException.class.isAssignableFrom(buildMethod.getReturnType()));
    }

    @Test
    public void testCanThrowAndCatch() {
        ViolationException exception = new ViolationException(testViolations);
        
        try {
            throw exception;
        } catch (ViolationException e) {
            assertEquals("捕获的异常应该是同一个", exception, e);
            assertEquals("violations应该正确", testViolations, e.getViolations());
        }
    }
}
