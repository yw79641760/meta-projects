package com.softmegatron.shared.meta.data.base;

import java.util.Arrays;

/**
 * ValueObject
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026-02-15 19:41:00
 * @version 1.0.0
 * @since 1.0.0
 */

public abstract class ValueObject extends BaseModel{

    /**
     * 获取相等属性
     * @return
     */
    protected abstract Object[] getEqualityComponents();

    @Override
    public boolean equals(Object obj) { 
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ValueObject other = (ValueObject) obj;
        return Arrays.equals(getEqualityComponents(), other.getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getEqualityComponents());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + Arrays.toString(getEqualityComponents());
    }
}
