package com.softmegatron.shared.meta.data.ext.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * MoneyUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 MoneyUtils 的各种功能
 * @date 2026/2/6 15:00
 * @since 1.0.0
 */
public class MoneyUtilsTest {

    @Test
    public void testGetAmountFromAmountCents() {
        String amount = MoneyUtils.getAmountFromAmountCents(100L);
        assertNotNull("金额不应为null", amount);
        assertEquals("100分应为1元", "1.00", amount);
    }

    @Test
    public void testGetAmountFromAmountCentsWithZero() {
        String amount = MoneyUtils.getAmountFromAmountCents(0L);
        assertNotNull("金额不应为null", amount);
        assertEquals("0分应为0元", "0.00", amount);
    }

    @Test
    public void testGetAmountFromAmountCentsWithLargeValue() {
        String amount = MoneyUtils.getAmountFromAmountCents(123456789L);
        assertNotNull("金额不应为null", amount);
        assertEquals("123456789分应为1234567.89元", "1234567.89", amount);
    }

    @Test
    public void testGetAmountFromAmountCentsWithNegativeValue() {
        String amount = MoneyUtils.getAmountFromAmountCents(-100L);
        assertNotNull("金额不应为null", amount);
        assertEquals("-100分应为-1.00元", "-1.00", amount);
    }

    @Test
    public void testGetAmountCentsFromAmount() {
        Long cents = MoneyUtils.getAmountCentsFromAmount("1.00");
        assertNotNull("分不应为null", cents);
        assertEquals("1元应为100分", Long.valueOf(100L), cents);
    }

    @Test
    public void testGetAmountCentsFromAmountWithZero() {
        Long cents = MoneyUtils.getAmountCentsFromAmount("0.00");
        assertNotNull("分不应为null", cents);
        assertEquals("0元应为0分", Long.valueOf(0L), cents);
    }

    @Test
    public void testGetAmountCentsFromAmountWithLargeValue() {
        Long cents = MoneyUtils.getAmountCentsFromAmount("1234567.89");
        assertNotNull("分不应为null", cents);
        assertEquals("1234567.89元应为123456789分", Long.valueOf(123456789L), cents);
    }

    @Test
    public void testGetAmountCentsFromAmountWithNegativeValue() {
        Long cents = MoneyUtils.getAmountCentsFromAmount("-1.00");
        assertNotNull("分不应为null", cents);
        assertEquals("-1元应为-100分", Long.valueOf(-100L), cents);
    }

    @Test
    public void testGetAmountCentsFromAmountWithNoCents() {
        Long cents = MoneyUtils.getAmountCentsFromAmount("1");
        assertNotNull("分不应为null", cents);
        assertEquals("1元应为100分", Long.valueOf(100L), cents);
    }

    @Test
    public void testGetPrettyAmount() {
        String amount = MoneyUtils.getPrettyAmount(100L);
        assertNotNull("金额不应为null", amount);
        assertEquals("100分应显示为1", "1", amount);
    }

    @Test
    public void testGetPrettyAmountWithCents() {
        String amount = MoneyUtils.getPrettyAmount(150L);
        assertNotNull("金额不应为null", amount);
        assertEquals("150分应显示为1.5", "1.5", amount);
    }

    @Test
    public void testGetPrettyAmountWithMultipleCents() {
        String amount = MoneyUtils.getPrettyAmount(1500L);
        assertNotNull("金额不应为null", amount);
        assertEquals("1500分应显示为15", "15", amount);
    }

    @Test
    public void testGetPrettyAmountWithTrailingZeros() {
        String amount = MoneyUtils.getPrettyAmount(1000L);
        assertNotNull("金额不应为null", amount);
        assertEquals("1000分应显示为10", "10", amount);
    }

    @Test
    public void testGetPrettyAmountWithZero() {
        String amount = MoneyUtils.getPrettyAmount(0L);
        assertNotNull("金额不应为null", amount);
        assertEquals("0分应显示为0", "0", amount);
    }

    @Test
    public void testGetPrettyAmountWithLargeValue() {
        String amount = MoneyUtils.getPrettyAmount(123456789L);
        assertNotNull("金额不应为null", amount);
        assertEquals("123456789分应显示为1234567.89", "1234567.89", amount);
    }

    @Test
    public void testGetPrettyAmountWithNegativeValue() {
        String amount = MoneyUtils.getPrettyAmount(-100L);
        assertNotNull("金额不应为null", amount);
        assertEquals("-100分应显示为-1", "-1", amount);
    }

    @Test
    public void testRoundTripConversion() {
        Long originalCents = 12345L;
        String amount = MoneyUtils.getAmountFromAmountCents(originalCents);
        Long backToCents = MoneyUtils.getAmountCentsFromAmount(amount);
        assertEquals("往返转换应保持一致", originalCents, backToCents);
    }

    @Test
    public void testRoundTripConversionWithPretty() {
        Long originalCents = 12345L;
        String prettyAmount = MoneyUtils.getPrettyAmount(originalCents);
        Long backToCents = MoneyUtils.getAmountCentsFromAmount(prettyAmount);
        assertEquals("美化金额往返转换应保持一致", originalCents, backToCents);
    }

    @Test
    public void testGetAmountFromAmountCentsWithOneCent() {
        String amount = MoneyUtils.getAmountFromAmountCents(1L);
        assertNotNull("金额不应为null", amount);
        assertEquals("1分应为0.01元", "0.01", amount);
    }

    @Test
    public void testGetAmountFromAmountCentsWithTenCents() {
        String amount = MoneyUtils.getAmountFromAmountCents(10L);
        assertNotNull("金额不应为null", amount);
        assertEquals("10分应为0.1元", "0.10", amount);
    }

    @Test
    public void testGetPrettyAmountWithOneCent() {
        String amount = MoneyUtils.getPrettyAmount(1L);
        assertNotNull("金额不应为null", amount);
        assertEquals("1分应显示为0.01", "0.01", amount);
    }

    @Test
    public void testGetPrettyAmountWithTenCents() {
        String amount = MoneyUtils.getPrettyAmount(10L);
        assertNotNull("金额不应为null", amount);
        assertEquals("10分应显示为0.1", "0.1", amount);
    }
}