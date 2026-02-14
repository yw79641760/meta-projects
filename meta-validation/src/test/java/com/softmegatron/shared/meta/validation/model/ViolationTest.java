package com.softmegatron.shared.meta.validation.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Violation 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 Violation 模型类
 * @date 2026/2/6 16:20
 * @since 1.0.0
 */
public class ViolationTest {

    private static final String TEST_MESSAGE = "Test violation message";
    private static final String TEST_PROPERTY_PATH = "fieldName";
    private static final Object TEST_TARGET = new Object();
    private static final Object TEST_INVALID_VALUE = "invalid";
    private static final String NULL_PROPERTY_PATH = null;
    private static final Object NULL_TARGET = null;
    private static final Object NULL_INVALID_VALUE = null;

    private Violation violation;

    @Before
    public void setUp() {
        violation = new Violation(TEST_MESSAGE, TEST_TARGET, TEST_PROPERTY_PATH, TEST_INVALID_VALUE);
    }

    @Test
    public void testConstructor() {
        assertNotNull("Violation不应该为null", violation);
        assertEquals("message应该正确", TEST_MESSAGE, violation.getMessage());
        assertEquals("target应该正确", TEST_TARGET, violation.getTarget());
        assertEquals("propertyPath应该正确", TEST_PROPERTY_PATH, violation.getPropertyPath());
        assertEquals("invalidValue应该正确", TEST_INVALID_VALUE, violation.getInvalidValue());
    }

    @Test
    public void testGetMessage() {
        String message = violation.getMessage();
        assertEquals("message应该正确", TEST_MESSAGE, message);
    }

    @Test
    public void testSetMessage() {
        String newMessage = "New message";
        violation.setMessage(newMessage);
        assertEquals("message应该被更新", newMessage, violation.getMessage());
    }

    @Test
    public void testGetTarget() {
        Object target = violation.getTarget();
        assertEquals("target应该正确", TEST_TARGET, target);
    }

    @Test
    public void testSetTarget() {
        Object newTarget = new Object();
        violation.setTarget(newTarget);
        assertEquals("target应该被更新", newTarget, violation.getTarget());
    }

    @Test
    public void testGetPropertyPath() {
        String propertyPath = violation.getPropertyPath();
        assertEquals("propertyPath应该正确", TEST_PROPERTY_PATH, propertyPath);
    }

    @Test
    public void testSetPropertyPath() {
        String newPropertyPath = "newProperty";
        violation.setPropertyPath(newPropertyPath);
        assertEquals("propertyPath应该被更新", newPropertyPath, violation.getPropertyPath());
    }

    @Test
    public void testGetInvalidValue() {
        Object invalidValue = violation.getInvalidValue();
        assertEquals("invalidValue应该正确", TEST_INVALID_VALUE, invalidValue);
    }

    @Test
    public void testSetInvalidValue() {
        Object newInvalidValue = "newInvalid";
        violation.setInvalidValue(newInvalidValue);
        assertEquals("invalidValue应该被更新", newInvalidValue, violation.getInvalidValue());
    }

    @Test
    public void testConstructorWithNullValues() {
        Violation nullViolation = new Violation(null, null, null, null);
        
        assertNotNull("Violation不应该为null", nullViolation);
        assertNull("message应该为null", nullViolation.getMessage());
        assertNull("target应该为null", nullViolation.getTarget());
        assertNull("propertyPath应该为null", nullViolation.getPropertyPath());
        assertNull("invalidValue应该为null", nullViolation.getInvalidValue());
    }

    @Test
    public void testConstructorWithMixedNullValues() {
        Violation mixedViolation = new Violation(TEST_MESSAGE, NULL_TARGET, NULL_PROPERTY_PATH, NULL_INVALID_VALUE);
        
        assertNotNull("Violation不应该为null", mixedViolation);
        assertEquals("message应该正确", TEST_MESSAGE, mixedViolation.getMessage());
        assertNull("target应该为null", mixedViolation.getTarget());
        assertNull("propertyPath应该为null", mixedViolation.getPropertyPath());
        assertNull("invalidValue应该为null", mixedViolation.getInvalidValue());
    }

    @Test
    public void testConstructorWithEmptyStringMessage() {
        String emptyMessage = "";
        Violation emptyMessageViolation = new Violation(emptyMessage, TEST_TARGET, TEST_PROPERTY_PATH, TEST_INVALID_VALUE);
        
        assertNotNull("Violation不应该为null", emptyMessageViolation);
        assertEquals("message应该是空字符串", emptyMessage, emptyMessageViolation.getMessage());
    }

    @Test
    public void testExtendsBaseModel() {
        assertTrue("Violation应该继承BaseModel", violation instanceof com.softmegatron.shared.meta.data.base.BaseModel);
    }

    @Test
    public void testHasSerialVersionUID() throws NoSuchFieldException {
        var serialVersionUidField = Violation.class.getDeclaredField("serialVersionUID");
        assertNotNull("serialVersionUID字段应该存在", serialVersionUidField);
        assertTrue("serialVersionUID应该是long类型", long.class == serialVersionUidField.getType());
        assertTrue("serialVersionUID应该是final", 
                  java.lang.reflect.Modifier.isFinal(serialVersionUidField.getModifiers()));
        assertTrue("serialVersionUID应该是static", 
                  java.lang.reflect.Modifier.isStatic(serialVersionUidField.getModifiers()));
    }

    @Test
    public void testIsPublicClass() {
        int modifiers = Violation.class.getModifiers();
        assertTrue("Violation应该是public类", 
                  java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    public void testIsNotAbstract() {
        int modifiers = Violation.class.getModifiers();
        assertFalse("Violation不应该是抽象类", 
                    java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    public void testAllFieldsArePrivate() throws NoSuchFieldException {
        var messageField = Violation.class.getDeclaredField("message");
        var targetField = Violation.class.getDeclaredField("target");
        var propertyPathField = Violation.class.getDeclaredField("propertyPath");
        var invalidValueField = Violation.class.getDeclaredField("invalidValue");
        
        assertTrue("message应该是private", 
                  java.lang.reflect.Modifier.isPrivate(messageField.getModifiers()));
        assertTrue("target应该是private", 
                  java.lang.reflect.Modifier.isPrivate(targetField.getModifiers()));
        assertTrue("propertyPath应该是private", 
                  java.lang.reflect.Modifier.isPrivate(propertyPathField.getModifiers()));
        assertTrue("invalidValue应该是private", 
                  java.lang.reflect.Modifier.isPrivate(invalidValueField.getModifiers()));
    }
}
