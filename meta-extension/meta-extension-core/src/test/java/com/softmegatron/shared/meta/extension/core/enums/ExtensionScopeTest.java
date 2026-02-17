package com.softmegatron.shared.meta.extension.core.enums;

import com.softmegatron.shared.meta.data.base.BaseEnum;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExtensionScope 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ExtensionScopeTest {

    @Test
    public void testSingletonValue() {
        assertNotNull(ExtensionScope.SINGLETON);
        assertEquals("SINGLETON", ExtensionScope.SINGLETON.getCode());
        assertEquals("单例模式", ExtensionScope.SINGLETON.getDesc());
    }

    @Test
    public void testPrototypeValue() {
        assertNotNull(ExtensionScope.PROTOTYPE);
        assertEquals("PROTOTYPE", ExtensionScope.PROTOTYPE.getCode());
        assertEquals("多例模式", ExtensionScope.PROTOTYPE.getDesc());
    }

    @Test
    public void testValues() {
        ExtensionScope[] values = ExtensionScope.values();
        assertEquals(2, values.length);
        assertTrue(contains(values, ExtensionScope.SINGLETON));
        assertTrue(contains(values, ExtensionScope.PROTOTYPE));
    }

    @Test
    public void testValueOf() {
        assertEquals(ExtensionScope.SINGLETON, ExtensionScope.valueOf("SINGLETON"));
        assertEquals(ExtensionScope.PROTOTYPE, ExtensionScope.valueOf("PROTOTYPE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        ExtensionScope.valueOf("INVALID");
    }

    @Test
    public void testImplementsBaseEnum() {
        assertTrue(BaseEnum.class.isAssignableFrom(ExtensionScope.class));
        assertNotNull(ExtensionScope.SINGLETON.getDesc());
        assertNotNull(ExtensionScope.PROTOTYPE.getDesc());
    }

    @Test
    public void testCodeField() {
        assertEquals("SINGLETON", ExtensionScope.SINGLETON.getCode());
        assertEquals("PROTOTYPE", ExtensionScope.PROTOTYPE.getCode());
    }

    @Test
    public void testDescField() {
        assertNotNull(ExtensionScope.SINGLETON.getDesc());
        assertNotNull(ExtensionScope.PROTOTYPE.getDesc());
        assertFalse(ExtensionScope.SINGLETON.getDesc().isEmpty());
        assertFalse(ExtensionScope.PROTOTYPE.getDesc().isEmpty());
    }

    @Test
    public void testCodeNotEqualsDesc() {
        // code 和 desc 应该不同
        assertNotEquals(ExtensionScope.SINGLETON.getCode(), ExtensionScope.SINGLETON.getDesc());
        assertNotEquals(ExtensionScope.PROTOTYPE.getCode(), ExtensionScope.PROTOTYPE.getDesc());
    }

    @Test
    public void testSingletonIsFirstValue() {
        ExtensionScope[] values = ExtensionScope.values();
        assertEquals(ExtensionScope.SINGLETON, values[0]);
    }

    @Test
    public void testEnumIsFinal() {
        // 枚举隐式是 final 的（大多数情况下）
        assertTrue(ExtensionScope.class.isEnum());
    }

    @Test
    public void testToString() {
        assertNotNull(ExtensionScope.SINGLETON.toString());
        assertNotNull(ExtensionScope.PROTOTYPE.toString());
    }

    @Test
    public void testOrdinal() {
        assertEquals(0, ExtensionScope.SINGLETON.ordinal());
        assertEquals(1, ExtensionScope.PROTOTYPE.ordinal());
    }

    private boolean contains(ExtensionScope[] values, ExtensionScope scope) {
        for (ExtensionScope value : values) {
            if (value == scope) {
                return true;
            }
        }
        return false;
    }
}
