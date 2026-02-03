package com.softmegatron.meta.commons.data.model.biz.currency;

import com.softmegatron.meta.commons.data.base.BaseModel;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Money
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:33 AM
 */
public class Money extends BaseModel implements Comparable<Money>{

    private static final long serialVersionUID = -2997496742424061921L;

    private static final String DEFAULT_CURRENCY_CODE = "CNY";

    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    private static final int[] CENT_FACTORS = new int[] {1, 10, 100, 1000};

    private static final String DEFAULT_LOCALE = "zh_CN";

    public static final Money ZERO = new Money(0);

    private long cent;

    private Currency currency;

    public Money() {
        this(0L);
    }

    public Money(long yuan, int cent) {
        this(yuan, cent, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    public Money(long cent) {
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
        this.cent = cent;
    }

    public Money(long yuan, int cent, Currency currency) {
        this.currency = currency;
        this.cent = (yuan * getCentFactor()) + (cent % getCentFactor());
    }

    public Money(String amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    public Money(String amount, Currency currency, int roundingMode) {
        this(new BigDecimal(amount), currency, roundingMode);
    }

    public Money(double amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.cent = Math.round(amount * getCentFactor());
    }

    public Money(BigDecimal amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    public Money(BigDecimal amount, int roundingMode) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE), roundingMode);
    }

    public Money(BigDecimal amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING_MODE);
    }

    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency = currency;
        this.cent = rounding(amount.movePointRight(currency.getDefaultFractionDigits()), roundingMode);
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.cent = rounding(amount.movePointRight(2), BigDecimal.ROUND_HALF_EVEN);
        }
    }

    public long getCent() {
        return this.cent;
    }

    public void setCent(long cent) {
        this.cent = cent;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public String getCurrencyCode() {
        return this.currency.getCurrencyCode();
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(this.cent, this.currency.getDefaultFractionDigits());
    }

    public int getCentFactor() {
        return CENT_FACTORS[this.currency.getDefaultFractionDigits()];
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Money) && equals((Money) other);
    }

    public boolean equals(Money other) {
        return this.currency.equals(other.currency) && (this.cent == other.cent);
    }

    @Override
    public int hashCode() {
        return (int) (this.cent ^ (this.cent >>> 32));
    }

    @Override
    public int compareTo(Money other) {
        assertSameCurrencyAs(other);
        if (this.cent < other.cent) {
            return -1;
        } else if (this.cent == other.cent) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public Money add(Money other) {
        assertSameCurrencyAs(other);
        return newMoneyWithSameCurrency(this.cent + other.cent);
    }

    public Money addTo(Money other) {
        assertSameCurrencyAs(other);
        this.cent += other.cent;
        return this;
    }

    public Money substract(Money other) {
        assertSameCurrencyAs(other);
        return newMoneyWithSameCurrency(this.cent - other.cent);
    }

    public Money substractFrom(Money other) {
        assertSameCurrencyAs(other);
        this.cent -= other.cent;
        return this;
    }

    public Money multiply(long val) {
        return newMoneyWithSameCurrency(this.cent * val);
    }

    public Money multiplyBy(long val) {
        this.cent *= val;
        return this;
    }

    public Money multiply(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent * val));
    }

    public Money multiplyBy(double val) {
        this.cent = Math.round(this.cent * val);
        return this;
    }

    public Money multiply(BigDecimal val) {
        return multiply(val, DEFAULT_ROUNDING_MODE);
    }

    public Money multiplyBy(BigDecimal val) {
        return multiplyBy(val, DEFAULT_ROUNDING_MODE);
    }

    public Money multiply(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);
        return newMoneyWithSameCurrency(rounding(newCent, roundingMode));
    }

    public Money multiplyBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);
        this.cent = rounding(newCent, roundingMode);
        return this;
    }

    public Money divide(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent / val));
    }

    public Money divideBy(double val) {
        this.cent = Math.round(this.cent / val);
        return this;
    }

    public Money divide(BigDecimal val) {
        return divide(val, DEFAULT_ROUNDING_MODE);
    }

    public Money divide(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);
        return newMoneyWithSameCurrency(newCent.longValue());
    }

    public Money divideBy(BigDecimal val) {
        return divideBy(val, DEFAULT_ROUNDING_MODE);
    }

    public Money divideBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);
        this.cent = newCent.longValue();
        return this;
    }

    @Override
    public String toString() {
        return getAmount().toString();
    }

    protected void assertSameCurrencyAs(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    protected long rounding(BigDecimal val, int roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    protected Money newMoneyWithSameCurrency(long cent) {
        Money money = new Money(0, this.currency);
        money.cent = cent;
        return money;
    }

    public boolean largeThanZero() {
        return this.cent > 0;
    }
}
