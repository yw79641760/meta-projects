package com.softmegatron.shared.meta.commons.data.ext.biz.currency;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

/**
 * Money 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 Money 的各种功能
 * @date 2026/2/6 16:30
 * @since 1.0.0
 */
public class MoneyTest {

    private static final Currency CNY = Currency.getInstance("CNY");

    @Test
    public void testConstructorWithZero() {
        Money money = new Money(0);
        assertEquals("0分应为0.00元", BigDecimal.valueOf(0, 2), money.getAmount());
    }

    @Test
    public void testConstructorWithCents() {
        Money money = new Money(100L);
        assertEquals("100分应为1.00元", BigDecimal.valueOf(100, 2), money.getAmount());
    }

    @Test
    public void testConstructorWithYuanAndCents() {
        Money money = new Money(10, 50);
        assertEquals("10元50分应为10.50元", BigDecimal.valueOf(1050, 2), money.getAmount());
    }

    @Test
    public void testConstructorWithString() {
        Money money = new Money("10.50");
        assertEquals("字符串10.50应为10.50元", BigDecimal.valueOf(1050, 2), money.getAmount());
    }

    @Test
    public void testGetCent() {
        Money money = new Money("10.50");
        assertEquals("getCent应返回1050", 1050L, money.getCent());
    }

    @Test
    public void testSetCent() {
        Money money = new Money(0);
        money.setCent(1050L);
        assertEquals("setCent后应正确", 1050L, money.getCent());
    }

    @Test
    public void testToString() {
        Money money = new Money("10.50");
        assertEquals("toString应返回10.50", "10.50", money.toString());
    }

    @Test
    public void testEqualsWithSameValue() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("10.50");
        assertTrue("相同值的钱应相等", money1.equals(money2));
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("20.00");
        assertFalse("不同值的钱不应相等", money1.equals(money2));
    }

    @Test
    public void testHashCode() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("10.50");
        assertEquals("相同值的hashCode应相同", money1.hashCode(), money2.hashCode());
    }

    @Test
    public void testCompareToLessThan() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("20.00");
        assertTrue("10.50应小于20.00", money1.compareTo(money2) < 0);
    }

    @Test
    public void testCompareToEqual() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("10.50");
        assertEquals("相同值应返回0", 0, money1.compareTo(money2));
    }

    @Test
    public void testGreaterThanTrue() {
        Money money1 = new Money("20.00");
        Money money2 = new Money("10.50");
        assertTrue("20.00应大于10.50", money1.greaterThan(money2));
    }

    @Test
    public void testGreaterThanFalse() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("20.00");
        assertFalse("10.50不应大于20.00", money1.greaterThan(money2));
    }

    @Test
    public void testAdd() {
        Money money1 = new Money("10.50");
        Money money2 = new Money("20.00");
        Money result = money1.add(money2);
        assertEquals("10.50+20.00应为30.50", BigDecimal.valueOf(3050, 2), result.getAmount());
    }

    @Test
    public void testSubstract() {
        Money money1 = new Money("20.00");
        Money money2 = new Money("10.50");
        Money result = money1.substract(money2);
        assertEquals("20.00-10.50应为9.50", BigDecimal.valueOf(950, 2), result.getAmount());
    }

    @Test
    public void testMultiplyByLong() {
        Money money = new Money("10.50");
        Money result = money.multiply(2);
        assertEquals("10.50*2应为21.00", BigDecimal.valueOf(2100, 2), result.getAmount());
    }

    @Test
    public void testMultiplyByDouble() {
        Money money = new Money("10.00");
        Money result = money.multiply(1.5);
        assertEquals("10.00*1.5应为15.00", BigDecimal.valueOf(1500, 2), result.getAmount());
    }

    @Test
    public void testDivideByDouble() {
        Money money = new Money("30.00");
        Money result = money.divide(2.0);
        assertEquals("30.00/2应为15.00", BigDecimal.valueOf(1500, 2), result.getAmount());
    }

    @Test
    public void testLargeThanZeroTrue() {
        Money money = new Money("10.50");
        assertTrue("10.50应大于0", money.largeThanZero());
    }

    @Test
    public void testLargeThanZeroFalse() {
        Money money = new Money("0.00");
        assertFalse("0.00不应大于0", money.largeThanZero());
    }

    @Test
    public void testZeroConstant() {
        assertNotNull("ZERO常量不应为null", Money.ZERO);
        assertTrue("ZERO的getAmount应为0", BigDecimal.ZERO.compareTo(Money.ZERO.getAmount()) == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithDifferentCurrency() {
        Money money1 = new Money("10.50", CNY);
        Money money2 = new Money("10.50", Currency.getInstance("USD"));
        money1.add(money2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubstractWithDifferentCurrency() {
        Money money1 = new Money("20.00", CNY);
        Money money2 = new Money("10.50", Currency.getInstance("USD"));
        money1.substract(money2);
    }

    @Test
    public void testRoundingWithFraction() {
        Money money = new Money("10.555");
        assertEquals("10.555应四舍五入为10.56", BigDecimal.valueOf(1056, 2), money.getAmount());
    }

    @Test
    public void testRoundingDown() {
        Money money = new Money("10.554");
        assertEquals("10.554应四舍五入为10.55", BigDecimal.valueOf(1055, 2), money.getAmount());
    }

    @Test
    public void testVeryLargeValue() {
        Money money = new Money("999999999.99");
        assertEquals("大数值应正确", BigDecimal.valueOf(99999999999L, 2), money.getAmount());
    }

    @Test
    public void testVerySmallValue() {
        Money money = new Money("0.01");
        assertEquals("最小值0.01应正确", BigDecimal.valueOf(1, 2), money.getAmount());
    }
}
