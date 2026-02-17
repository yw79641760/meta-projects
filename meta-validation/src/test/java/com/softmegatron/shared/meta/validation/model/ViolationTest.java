package com.softmegatron.shared.meta.validation.model;

import com.softmegatron.shared.meta.data.base.BaseModel;
import com.softmegatron.shared.meta.data.base.BaseSerializable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Violation 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class ViolationTest {

    private static final String TEST_MESSAGE = "Test violation message";
    private static final String TEST_PROPERTY_PATH = "fieldName";
    private static final Object TEST_TARGET = new Object();
    private static final Object TEST_INVALID_VALUE = "invalid";

    private Violation violation;

    @Before
    public void setUp() {
        violation = new Violation(TEST_MESSAGE, TEST_TARGET, TEST_PROPERTY_PATH, TEST_INVALID_VALUE);
    }

    @Test
    public void testConstructor() {
        assertNotNull(violation);
        assertEquals(TEST_MESSAGE, violation.getMessage());
        assertEquals(TEST_TARGET, violation.getTarget());
        assertEquals(TEST_PROPERTY_PATH, violation.getPropertyPath());
        assertEquals(TEST_INVALID_VALUE, violation.getInvalidValue());
    }

    @Test
    public void testGetMessage() {
        assertEquals(TEST_MESSAGE, violation.getMessage());
    }

    @Test
    public void testSetMessage() {
        String newMessage = "New message";
        violation.setMessage(newMessage);
        assertEquals(newMessage, violation.getMessage());
    }

    @Test
    public void testGetTarget() {
        assertEquals(TEST_TARGET, violation.getTarget());
    }

    @Test
    public void testSetTarget() {
        Object newTarget = new Object();
        violation.setTarget(newTarget);
        assertEquals(newTarget, violation.getTarget());
    }

    @Test
    public void testGetPropertyPath() {
        assertEquals(TEST_PROPERTY_PATH, violation.getPropertyPath());
    }

    @Test
    public void testSetPropertyPath() {
        String newPath = "newPath";
        violation.setPropertyPath(newPath);
        assertEquals(newPath, violation.getPropertyPath());
    }

    @Test
    public void testGetInvalidValue() {
        assertEquals(TEST_INVALID_VALUE, violation.getInvalidValue());
    }

    @Test
    public void testSetInvalidValue() {
        Object newValue = "newValue";
        violation.setInvalidValue(newValue);
        assertEquals(newValue, violation.getInvalidValue());
    }

    @Test
    public void testConstructorWithNullValues() {
        Violation nullViolation = new Violation(null, null, null, null);

        assertNull(nullViolation.getMessage());
        assertNull(nullViolation.getTarget());
        assertNull(nullViolation.getPropertyPath());
        assertNull(nullViolation.getInvalidValue());
    }

    @Test
    public void testConstructorWithEmptyStringMessage() {
        Violation emptyMessageViolation = new Violation("", TEST_TARGET, TEST_PROPERTY_PATH, TEST_INVALID_VALUE);
        assertEquals("", emptyMessageViolation.getMessage());
    }

    @Test
    public void testConstructorWithEmptyStringPropertyPath() {
        Violation emptyPathViolation = new Violation(TEST_MESSAGE, TEST_TARGET, "", TEST_INVALID_VALUE);
        assertEquals("", emptyPathViolation.getPropertyPath());
    }

    @Test
    public void testExtendsBaseModel() {
        assertTrue(violation instanceof BaseModel);
    }

    @Test
    public void testExtendsBaseSerializable() {
        assertTrue(violation instanceof BaseSerializable);
    }

    @Test
    public void testHasSerialVersionUID() throws NoSuchFieldException {
        var field = Violation.class.getDeclaredField("serialVersionUID");
        assertTrue(java.lang.reflect.Modifier.isStatic(field.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()));
        assertEquals(long.class, field.getType());
    }

    @Test
    public void testIsPublicClass() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Violation.class.getModifiers()));
    }

    @Test
    public void testIsNotAbstract() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(Violation.class.getModifiers()));
    }

    @Test
    public void testAllFieldsArePrivate() throws NoSuchFieldException {
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                Violation.class.getDeclaredField("message").getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                Violation.class.getDeclaredField("target").getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                Violation.class.getDeclaredField("propertyPath").getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                Violation.class.getDeclaredField("invalidValue").getModifiers()));
    }

    @Test
    public void testMessageWithDifferentTypes() {
        Violation intViolation = new Violation("Integer violation", TEST_TARGET, "count", 123);
        assertEquals(123, intViolation.getInvalidValue());

        Violation boolViolation = new Violation("Boolean violation", TEST_TARGET, "active", true);
        assertEquals(true, boolViolation.getInvalidValue());

        Violation nullViolation = new Violation("Null violation", TEST_TARGET, "value", null);
        assertNull(nullViolation.getInvalidValue());
    }

    @Test
    public void testSetMessageToNull() {
        violation.setMessage(null);
        assertNull(violation.getMessage());
    }

    @Test
    public void testSetTargetToNull() {
        violation.setTarget(null);
        assertNull(violation.getTarget());
    }

    @Test
    public void testSetPropertyPathToNull() {
        violation.setPropertyPath(null);
        assertNull(violation.getPropertyPath());
    }

    @Test
    public void testSetInvalidValueToNull() {
        violation.setInvalidValue(null);
        assertNull(violation.getInvalidValue());
    }

    @Test
    public void testMultipleSetOperations() {
        violation.setMessage("Updated message");
        violation.setTarget("Updated target");
        violation.setPropertyPath("updated.path");
        violation.setInvalidValue(999);

        assertEquals("Updated message", violation.getMessage());
        assertEquals("Updated target", violation.getTarget());
        assertEquals("updated.path", violation.getPropertyPath());
        assertEquals(999, violation.getInvalidValue());
    }

    @Test
    public void testWithComplexInvalidValue() {
        Object complexValue = new int[]{1, 2, 3};
        Violation complexViolation = new Violation("Complex value", TEST_TARGET, "array", complexValue);
        assertArrayEquals(new int[]{1, 2, 3}, (int[]) complexViolation.getInvalidValue());
    }

    @Test
    public void testWithCollectionInvalidValue() {
        java.util.List<String> listValue = java.util.Arrays.asList("a", "b", "c");
        Violation listViolation = new Violation("List value", TEST_TARGET, "list", listValue);
        assertEquals(listValue, listViolation.getInvalidValue());
    }

    @Test
    public void testPropertyPathWithNestedPath() {
        Violation nestedViolation = new Violation(
                "Nested violation",
                TEST_TARGET,
                "address.city",
                "Invalid city"
        );
        assertEquals("address.city", nestedViolation.getPropertyPath());
    }

    @Test
    public void testPropertyPathWithArrayIndex() {
        Violation indexedViolation = new Violation(
                "Indexed violation",
                TEST_TARGET,
                "items[0].name",
                "Invalid name"
        );
        assertEquals("items[0].name", indexedViolation.getPropertyPath());
    }

    @Test
    public void testConstructorWithPrimitiveWrapper() {
        Violation integerViolation = new Violation("Integer value", TEST_TARGET, "count", Integer.valueOf(100));
        assertEquals(Integer.valueOf(100), integerViolation.getInvalidValue());

        Violation longViolation = new Violation("Long value", TEST_TARGET, "timestamp", Long.valueOf(123456789L));
        assertEquals(Long.valueOf(123456789L), longViolation.getInvalidValue());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        Violation emptyViolation = new Violation("", "", "", "");
        assertEquals("", emptyViolation.getMessage());
        assertEquals("", emptyViolation.getTarget());
        assertEquals("", emptyViolation.getPropertyPath());
        assertEquals("", emptyViolation.getInvalidValue());
    }
}
