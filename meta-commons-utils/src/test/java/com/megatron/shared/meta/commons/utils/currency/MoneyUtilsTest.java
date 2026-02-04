package com.megatron.shared.meta.commons.utils.currency;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * MoneyUtilsTest
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description
 * @date 2026/2/4 14:27
 * @since 1.0.0
 */
public class MoneyUtilsTest{

    @Test
    public void getAmountFromAmountCents() {
        assertNotNull(MoneyUtils.getAmountFromAmountCents(100L));
    }
}