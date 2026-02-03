package com.softmegatron.meta.commons.validation.model;

import com.softmegatron.meta.commons.data.base.BaseModel;

/**
 * Violation
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/6/20 1:17 AM
 */
public class Violation extends BaseModel {

    private static final long serialVersionUID = -767335543987600893L;
    /**
     * 异常信息
     */
    private String message;
    /**
     * 异常对象
     */
    private Object target;
    /**
     * 异常字段
     */
    private String propertyPath;
    /**
     * 异常值
     */
    private Object invalidValue;

    public Violation(String message, Object target, String propertyPath, Object invalidValue) {
        this.message = message;
        this.target = target;
        this.propertyPath = propertyPath;
        this.invalidValue = invalidValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }
}
