package com.softmegatron.shared.meta.core.utils;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * AppVersionUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:21 AM
 */
public class AppVersionUtils {

    private AppVersionUtils() {
    }

    private static final String VERSION_SEPARATOR_REGEX = "\\.";

    /**
     * 比较版本大小
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compare(String version1, String version2) {
       if (version1 == null || version1.isEmpty()) {
           return -1;
       }
       if (version2 == null || version2.isEmpty()) {
           return 1;
       }
       String[] verArr1 = version1.split(VERSION_SEPARATOR_REGEX);
       String[] verArr2 = version2.split(VERSION_SEPARATOR_REGEX);
       int length1 = verArr1.length;
       int length2 = verArr2.length;
       int compareLength = Math.min(length1, length2);
       for (int i = 0; i < compareLength; i++) {
           int v1 = NumberUtils.toInt(verArr1[i], 0);
           int v2 = NumberUtils.toInt(verArr2[i], 0);
           if (v1 != v2) {
               return v1 > v2 ? 1 : -1;
           }
       }
       return length1 - length2;
    }
}
