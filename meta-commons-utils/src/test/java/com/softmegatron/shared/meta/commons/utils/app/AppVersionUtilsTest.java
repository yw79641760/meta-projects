package com.softmegatron.shared.meta.commons.utils.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * AppVersionUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 AppVersionUtils 的各种功能
 * @date 2026/2/6 14:35
 * @since 1.0.0
 */
public class AppVersionUtilsTest {

    @Test
    public void testCompareWithSameVersions() {
        int result = AppVersionUtils.compare("1.0.0", "1.0.0");
        assertEquals("相同版本应返回0", 0, result);
    }

    @Test
    public void testCompareWithFirstVersionGreater() {
        int result = AppVersionUtils.compare("2.0.0", "1.0.0");
        assertTrue("第一个版本大应返回正数", result > 0);
    }

    @Test
    public void testCompareWithSecondVersionGreater() {
        int result = AppVersionUtils.compare("1.0.0", "2.0.0");
        assertTrue("第二个版本大应返回负数", result < 0);
    }

    @Test
    public void testCompareWithMinorVersionDifferent() {
        int result = AppVersionUtils.compare("1.2.0", "1.1.0");
        assertTrue("次版本大应返回正数", result > 0);
    }

    @Test
    public void testCompareWithPatchVersionDifferent() {
        int result = AppVersionUtils.compare("1.0.5", "1.0.3");
        assertTrue("修订版本大应返回正数", result > 0);
    }

    @Test
    public void testCompareWithFirstVersionNull() {
        int result = AppVersionUtils.compare(null, "1.0.0");
        assertEquals("第一个版本为null应返回-1", -1, result);
    }

    @Test
    public void testCompareWithFirstVersionEmpty() {
        int result = AppVersionUtils.compare("", "1.0.0");
        assertEquals("第一个版本为空应返回-1", -1, result);
    }

    @Test
    public void testCompareWithSecondVersionNull() {
        int result = AppVersionUtils.compare("1.0.0", null);
        assertEquals("第二个版本为null应返回1", 1, result);
    }

    @Test
    public void testCompareWithSecondVersionEmpty() {
        int result = AppVersionUtils.compare("1.0.0", "");
        assertEquals("第二个版本为空应返回1", 1, result);
    }

    @Test
    public void testCompareWithDifferentLengthVersions() {
        int result = AppVersionUtils.compare("1.0.0", "1.0");
        assertTrue("版本长度长应返回正数", result > 0);
    }

    @Test
    public void testCompareWithShorterVersionFirst() {
        int result = AppVersionUtils.compare("1.0", "1.0.0");
        assertTrue("版本长度短应返回负数", result < 0);
    }

    @Test
    public void testCompareWithNonNumericPart() {
        int result = AppVersionUtils.compare("1.0.a", "1.0.0");
        assertTrue("非数字部分应被视为0", result == 0);
    }

    @Test
    public void testCompareWithMajorVersionOnly() {
        int result = AppVersionUtils.compare("2", "1");
        assertTrue("主版本大应返回正数", result > 0);
    }

    @Test
    public void testCompareWithFourPartVersion() {
        int result = AppVersionUtils.compare("1.0.0.1", "1.0.0.0");
        assertTrue("第四部分大应返回正数", result > 0);
    }

    @Test
    public void testCompareWithZeroPadding() {
        int result1 = AppVersionUtils.compare("1.01", "1.1");
        assertEquals("前导零应被忽略", 0, result1);

        int result2 = AppVersionUtils.compare("1.001", "1.1");
        assertEquals("多个前导零应被忽略", 0, result2);
    }

    @Test
    public void testCompareWithLargeVersionNumbers() {
        int result = AppVersionUtils.compare("100.200.300", "99.199.299");
        assertTrue("大版本号比较应正确", result > 0);
    }
}
