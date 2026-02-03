package com.softmegatron.meta.commons.validation.utils;

import com.softmegatron.meta.commons.validation.exception.ViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * ValidatorUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/6/20 1:38 AM
 */
public class ValidatorUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtils.class);

    private static final Validator DEFAULT_VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 验证整体
     *
     * @param target
     * @param groups
     * @param <T>
     */
    public static <T> void validate(T target, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv = null;
        try {
            cv = DEFAULT_VALIDATOR.validate(target, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        } catch (TypeNotPresentException ex) {
            //do nothing
            LOGGER.warn("tryValidate {} got java.lang.TypeNotPresentException , ignored", target);
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * 验证字段
     *
     * @param object
     * @param propertyName
     * @param groups
     * @param <T>
     */
    public static <T> void validateProperty(T object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv;
        try {
            cv = DEFAULT_VALIDATOR.validateProperty(object, propertyName, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * 验证值
     *
     * @param beanType
     * @param propertyName
     * @param value
     * @param groups
     * @param <T>
     */
    public static <T> void validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv;
        try {
            cv = DEFAULT_VALIDATOR.validateValue(beanType, propertyName, value, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * 验证整体
     *
     * @param target
     * @param groups
     * @param <T>
     */
    public static <T> void validate(Validator validator, T target, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv = null;
        try {
            cv = validator.validate(target, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        } catch (TypeNotPresentException ex) {
            //do nothing
            LOGGER.warn("tryValidate {} got java.lang.TypeNotPresentException , ignored", target);
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * 验证字段
     *
     * @param object
     * @param propertyName
     * @param groups
     * @param <T>
     */
    public static <T> void validateProperty(Validator validator, T object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv;
        try {
            cv = validator.validateProperty(object, propertyName, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * 验证值
     *
     * @param beanType
     * @param propertyName
     * @param value
     * @param groups
     * @param <T>
     */
    public static <T> void validateValue(Validator validator, Class<T> beanType,
                                         String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<T>> cv;
        try {
            cv = validator.validateValue(beanType, propertyName, value, groups);
        } catch (IllegalArgumentException | ValidationException ex) {
            throw ex;
        }
        if (cv != null && !cv.isEmpty()) {
            throw ViolationException.build(cv);
        }
    }

    /**
     * Convenient method for 'quick' validate a string value against a Regex.
     *
     * @param regex
     * @param value
     */
    public static void tryValidateRegex(String regex, String value) {
        try {
            if (!Pattern.compile(regex).matcher(value).matches()) {
                throw ViolationException.build(regex, value);
            }
        } catch (PatternSyntaxException ex) {
            LOGGER.error("Regex {} has syntax error.", regex);
        }
    }
}
