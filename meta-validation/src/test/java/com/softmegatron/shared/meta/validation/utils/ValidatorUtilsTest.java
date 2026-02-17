package com.softmegatron.shared.meta.validation.utils;

import com.softmegatron.shared.meta.validation.exception.ViolationException;
import com.softmegatron.shared.meta.validation.model.Violation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * ValidatorUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
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

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    private static class EmptyObject {
    }

    private interface CreateGroup {
    }

    private interface UpdateGroup {
    }

    private static class GroupTestObject {
        @NotNull(groups = CreateGroup.class)
        private String createOnly;

        @NotNull(groups = UpdateGroup.class)
        private String updateOnly;

        @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
        private String both;

        public GroupTestObject(String createOnly, String updateOnly, String both) {
            this.createOnly = createOnly;
            this.updateOnly = updateOnly;
            this.both = both;
        }
    }

    private static class NestedObject {
        @NotNull
        private String name;

        @Valid
        private TestObject nested;

        public NestedObject(String name, TestObject nested) {
            this.name = name;
            this.nested = nested;
        }
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
        } catch (ViolationException e) {
            assertNotNull(e.getViolations());
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithInvalidSize() {
        TestObject invalidObject = new TestObject("test", "very long description", 25, "test@example.com", "123");

        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithInvalidAge() {
        TestObject invalidObject = new TestObject("test", "valid", 15, "test@example.com", "123");

        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithInvalidEmail() {
        TestObject invalidObject = new TestObject("test", "valid", 25, "invalid-email", "123");

        try {
            ValidatorUtils.validate(invalidObject);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
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
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatePropertyWithNullObject() {
        ValidatorUtils.validateProperty(null, "name");
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
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
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
        } catch (ViolationException e) {
            Set<Violation> violations = e.getViolations();
            assertEquals(1, violations.size());
            Violation violation = violations.iterator().next();
            assertTrue(violation.getMessage().contains("not matching"));
        }
    }

    @Test
    public void testTryValidateRegexWithInvalidRegex() {
        ValidatorUtils.tryValidateRegex("[invalid", "123");
    }

    @Test
    public void testTryValidateRegexWithEmptyValue() {
        try {
            ValidatorUtils.tryValidateRegex("\\d+", "");
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testTryValidateRegexWithNullValue() {
        try {
            ValidatorUtils.tryValidateRegex("\\d+", null);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertNotNull(e);
        } catch (NullPointerException e) {
            // 或者抛出 NullPointerException
        }
    }

    @Test
    public void testValidateWithCustomValidator() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestObject validObject = new TestObject("test", "valid", 25, "test@example.com", "123");
        ValidatorUtils.validate(validator, validObject);
    }

    @Test
    public void testValidateWithCustomValidatorInvalid() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestObject invalidObject = new TestObject(null, "valid", 25, "test@example.com", "123");

        try {
            ValidatorUtils.validate(validator, invalidObject);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidatePropertyWithCustomValidator() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestObject object = new TestObject("test", "valid", 25, "test@example.com", "123");
        ValidatorUtils.validateProperty(validator, object, "name");
    }

    @Test
    public void testValidatePropertyWithCustomValidatorInvalid() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TestObject object = new TestObject(null, "valid", 25, "test@example.com", "123");

        try {
            ValidatorUtils.validateProperty(validator, object, "name");
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateValueWithCustomValidator() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        ValidatorUtils.validateValue(validator, TestObject.class, "age", 25);
    }

    @Test
    public void testValidateValueWithCustomValidatorInvalid() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        try {
            ValidatorUtils.validateValue(validator, TestObject.class, "age", 10);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithCreateGroup() {
        GroupTestObject object = new GroupTestObject("value", null, "both");
        ValidatorUtils.validate(object, CreateGroup.class);
    }

    @Test
    public void testValidateWithCreateGroupFails() {
        GroupTestObject object = new GroupTestObject(null, null, "both");

        try {
            ValidatorUtils.validate(object, CreateGroup.class);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithUpdateGroup() {
        GroupTestObject object = new GroupTestObject(null, "value", "both");
        ValidatorUtils.validate(object, UpdateGroup.class);
    }

    @Test
    public void testValidateWithUpdateGroupFails() {
        GroupTestObject object = new GroupTestObject(null, null, "both");

        try {
            ValidatorUtils.validate(object, UpdateGroup.class);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithBothGroups() {
        GroupTestObject object = new GroupTestObject("create", "update", "both");
        ValidatorUtils.validate(object, CreateGroup.class, UpdateGroup.class);
    }

    @Test
    public void testValidateWithBothGroupsFailsOnCreate() {
        GroupTestObject object = new GroupTestObject(null, "update", "both");

        try {
            ValidatorUtils.validate(object, CreateGroup.class, UpdateGroup.class);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithBothGroupsFailsOnBoth() {
        GroupTestObject object = new GroupTestObject("create", "update", null);

        try {
            ValidatorUtils.validate(object, CreateGroup.class, UpdateGroup.class);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithDefaultGroup() {
        TestObject object = new TestObject("test", "valid", 25, "test@example.com", "123");
        ValidatorUtils.validate(object, Default.class);
    }

    @Test
    public void testValidateValueWithNullValue() {
        try {
            ValidatorUtils.validateValue(TestObject.class, "name", null);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateMultipleViolations() {
        TestObject object = new TestObject(null, "x", 10, "invalid", "abc");

        try {
            ValidatorUtils.validate(object);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertTrue(e.getViolations().size() >= 3);
            String message = e.getMessage();
            assertNotNull(message);
            assertFalse(message.isEmpty());
        }
    }

    @Test
    public void testValidateWithMaxAge() {
        TestObject object = new TestObject("test", "valid", 121, "test@example.com", "123");

        try {
            ValidatorUtils.validate(object);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidateWithBoundaryAge() {
        TestObject object1 = new TestObject("test", "valid", 18, "test@example.com", "123");
        TestObject object2 = new TestObject("test", "valid", 120, "test@example.com", "123");

        ValidatorUtils.validate(object1);
        ValidatorUtils.validate(object2);
    }

    @Test
    public void testValidateWithBoundarySize() {
        TestObject object1 = new TestObject("test", "ab", 25, "test@example.com", "123");
        TestObject object2 = new TestObject("test", "1234567890", 25, "test@example.com", "123");

        ValidatorUtils.validate(object1);
        ValidatorUtils.validate(object2);
    }

    @Test
    public void testValidateWithSizeTooShort() {
        TestObject object = new TestObject("test", "a", 25, "test@example.com", "123");

        try {
            ValidatorUtils.validate(object);
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertFalse(e.getViolations().isEmpty());
        }
    }

    @Test
    public void testValidatePropertyWithNonexistentProperty() {
        TestObject object = new TestObject("test", "valid", 25, "test@example.com", "123");

        try {
            ValidatorUtils.validateProperty(object, "nonexistent");
            fail("应该抛出异常");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testValidateValueWithNonexistentProperty() {
        try {
            ValidatorUtils.validateValue(TestObject.class, "nonexistent", "value");
            fail("应该抛出异常");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testTryValidateRegexWithEmailPattern() {
        ValidatorUtils.tryValidateRegex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", "test@example.com");
    }

    @Test
    public void testTryValidateRegexWithEmailPatternInvalid() {
        try {
            ValidatorUtils.tryValidateRegex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", "invalid-email");
            fail("应该抛出ViolationException");
        } catch (ViolationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testTryValidateRegexWithPhonePattern() {
        ValidatorUtils.tryValidateRegex("^\\d{11}$", "13812345678");
    }

    @Test
    public void testValidatorUtilsHasPrivateConstructor() throws NoSuchMethodException {
        var constructor = ValidatorUtils.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
    }
}
