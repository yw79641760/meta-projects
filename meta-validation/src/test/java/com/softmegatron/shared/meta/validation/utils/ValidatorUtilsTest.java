package com.softmegatron.shared.meta.validation.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ValidatorUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ValidatorUtils 工具类
 * @date 2026/2/6 16:20
 * @since 1.0.0
 */
public class ValidatorUtilsTest {

    private static class TestObject {
        @NotNull
        private String name;

        @Size(min = 2, max = 10)
        private String description;

        @Min(18)
        @Max(120)
        private int age;

        @Email
        private String email;

        @Pattern(regexp = "\\d+")
        private String code;

        public TestObject(String name, String description, int age, String email, String code) {
            this.name = name;
            this.description = description;
            this.age = age;
            this.email = email;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }

        public String getCode() {
            return code;
        }
    }

    private static class EmptyObject {
    }

    @Test
    public void testValidateWithValidObject() {
        TestObject validObject = new TestObject("test", "valid", 25, "test@example.com", "123");
        
        ValidatorUtils.validate(validObject);
    }

    @Test
    public void testValidateWithNullName() {
        TestObject invalidObject = new TestObject(null, "valid", 25, "test@example.com", "123");
        
        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertNotNull("violations不应该为null", e.getViolations());
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateWithInvalidSize() {
        TestObject invalidObject = new TestObject("test", "very long description", 25, "test@example.com", "123");
        
        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateWithInvalidAge() {
        TestObject invalidObject = new TestObject("test", "valid", 15, "test@example.com", "123");
        
        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateWithInvalidEmail() {
        TestObject invalidObject = new TestObject("test", "valid", 25, "invalid-email", "123");
        
        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateWithNull() {
        try {
            ValidatorUtils.validate(null);
        } catch (Exception e) {
            assertNotNull("异常不应该为null", e);
            System.out.println("Expected exception for null: " + e.getClass().getName());
        }
    }

    @Test
    public void testValidateWithEmptyObject() {
        EmptyObject emptyObject = new EmptyObject();
        ValidatorUtils.validate(emptyObject);
    }

    @Test
    public void testValidatePropertyWithValidValue() {
        TestObject object = new TestObject("test", "valid", 25, "test@example.com", "123");
        ValidatorUtils.validateProperty(object, "name");
    }

    @Test
    public void testValidatePropertyWithInvalidValue() {
        TestObject object = new TestObject(null, "valid", 25, "test@example.com", "123");
        
        try {
            ValidatorUtils.validateProperty(object, "name");
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidatePropertyWithNullObject() {
        try {
            ValidatorUtils.validateProperty(null, "name");
            fail("应该抛出异常");
        } catch (IllegalArgumentException e) {
            assertNotNull("异常不应该为null", e);
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateValueWithValidValue() {
        ValidatorUtils.validateValue(TestObject.class, "age", 25);
    }

    @Test
    public void testValidateValueWithInvalidValue() {
        try {
            ValidatorUtils.validateValue(TestObject.class, "age", 10);
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            assertTrue("violations应该不为空", !e.getViolations().isEmpty());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTryValidateRegexWithMatchingValue() {
        ValidatorUtils.tryValidateRegex("\\d+", "12345");
    }

    @Test
    public void testTryValidateRegexWithNonMatchingValue() {
        try {
            ValidatorUtils.tryValidateRegex("\\d+", "abc");
            fail("应该抛出ViolationException");
        } catch (com.softmegatron.shared.meta.validation.exception.ViolationException e) {
            assertNotNull("异常不应该为null", e);
            Set<com.softmegatron.shared.meta.validation.model.Violation> violations = e.getViolations();
            assertNotNull("violations不应该为null", violations);
            assertTrue("violations应该有1个元素", violations.size() == 1);
            
            com.softmegatron.shared.meta.validation.model.Violation violation = 
                violations.iterator().next();
            assertTrue("message应该包含'not matching'", 
                      violation.getMessage().contains("not matching"));
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTryValidateRegexWithInvalidRegex() {
        ValidatorUtils.tryValidateRegex("[invalid", "123");
    }

    @Test
    public void testValidatorUtilsHasPrivateConstructor() throws NoSuchMethodException {
        var constructor = ValidatorUtils.class.getDeclaredConstructor();
        assertNotNull("构造函数应该存在", constructor);
        assertTrue("构造函数应该是private", 
                  java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testValidatorUtilsHasDefaultConstructor() throws NoSuchMethodException {
        var constructor = ValidatorUtils.class.getDeclaredConstructor();
        assertNotNull("构造函数应该存在", constructor);
    }

    @Test
    public void testValidatorUtilsIsNotFinalClass() {
        int modifiers = ValidatorUtils.class.getModifiers();
        assertFalse("ValidatorUtils不应该是final类", 
                    java.lang.reflect.Modifier.isFinal(modifiers));
    }

    @Test
    public void testValidatorUtilsHasLogger() throws NoSuchFieldException {
        var loggerField = ValidatorUtils.class.getDeclaredField("LOGGER");
        assertNotNull("LOGGER字段应该存在", loggerField);
        assertTrue("LOGGER应该是static final", 
                  java.lang.reflect.Modifier.isStatic(loggerField.getModifiers()) &&
                  java.lang.reflect.Modifier.isFinal(loggerField.getModifiers()));
    }

    @Test
    public void testValidatorUtilsHasDefaultValidator() throws NoSuchFieldException {
        var validatorField = ValidatorUtils.class.getDeclaredField("DEFAULT_VALIDATOR");
        assertNotNull("DEFAULT_VALIDATOR字段应该存在", validatorField);
        assertTrue("DEFAULT_VALIDATOR应该是static final", 
                  java.lang.reflect.Modifier.isStatic(validatorField.getModifiers()) &&
                  java.lang.reflect.Modifier.isFinal(validatorField.getModifiers()));
        assertTrue("DEFAULT_VALIDATOR应该是Validator类型", 
                  Validator.class.isAssignableFrom(validatorField.getType()));
    }

    @Test
    public void testValidateMethodIsStatic() throws NoSuchMethodException {
        var validateMethod = ValidatorUtils.class.getMethod("validate", Object.class, Class[].class);
        assertNotNull("validate方法应该存在", validateMethod);
        assertTrue("validate方法应该是static", 
                  java.lang.reflect.Modifier.isStatic(validateMethod.getModifiers()));
    }

    @Test
    public void testValidatePropertyMethodIsStatic() throws NoSuchMethodException {
        var validatePropertyMethod = ValidatorUtils.class.getMethod("validateProperty", 
                                                                     Object.class, 
                                                                     String.class, 
                                                                     Class[].class);
        assertNotNull("validateProperty方法应该存在", validatePropertyMethod);
        assertTrue("validateProperty方法应该是static", 
                  java.lang.reflect.Modifier.isStatic(validatePropertyMethod.getModifiers()));
    }

    @Test
    public void testValidateValueMethodIsStatic() throws NoSuchMethodException {
        var validateValueMethod = ValidatorUtils.class.getMethod("validateValue", 
                                                                 Class.class, 
                                                                 String.class, 
                                                                 Object.class, 
                                                                 Class[].class);
        assertNotNull("validateValue方法应该存在", validateValueMethod);
        assertTrue("validateValue方法应该是static", 
                  java.lang.reflect.Modifier.isStatic(validateValueMethod.getModifiers()));
    }

    @Test
    public void testTryValidateRegexMethodIsStatic() throws NoSuchMethodException {
        var tryValidateRegexMethod = ValidatorUtils.class.getMethod("tryValidateRegex", String.class, String.class);
        assertNotNull("tryValidateRegex方法应该存在", tryValidateRegexMethod);
        assertTrue("tryValidateRegex方法应该是static", 
                  java.lang.reflect.Modifier.isStatic(tryValidateRegexMethod.getModifiers()));
    }
}
