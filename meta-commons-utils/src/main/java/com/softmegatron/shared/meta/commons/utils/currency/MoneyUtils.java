package com.softmegatron.shared.meta.commons.utils.currency;

import com.softmegatron.shared.meta.commons.data.model.biz.currency.Money;

/**
 * MoneyUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:35 AM
 */
public class MoneyUtils {

    private static final String AMOUNT_DOT = ".";

    private MoneyUtils() {
    }

    /**
     * 将分转换为元
     *
     * @param amount
     * @return
     */
    public static String getAmountFromAmountCents(Long amount) {
        return new Money(amount).toString();
    }

    /**
     * 将元转换为分
     *
     * @param amount
     * @return
     */
    public static Long getAmountCentsFromAmount(String amount) {
        return new Money(amount).getCent();
    }

    /**
     * 去除小数点末尾的0或.0
     *
     * @param rawPrice
     * @return
     */
    private static String trimZeroAndDot(String rawPrice) {
        if (rawPrice.indexOf(AMOUNT_DOT) > 0) {
            // 去掉多余的0
            rawPrice = rawPrice.replaceAll("0+$", "");
            // 如最后一位是.则去掉
            rawPrice = rawPrice.replaceAll("[.]$", "");
        }
        return rawPrice;
    }

    /**
     * 获取展示用金额
     *
     * @param amount
     * @return
     */
    public static String getPrettyAmount(Long amount) {
        return trimZeroAndDot(new Money(amount).toString());
    }
}
