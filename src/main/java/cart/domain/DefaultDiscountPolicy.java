package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class DefaultDiscountPolicy implements DiscountPolicy {
    public static final String DISCOUNT_UNDER_ZERO_ERROR = "할인율은 0보다 작을 수 없습니다.";
    public static final String DISCOUNT_OVER_ONE_ERROR = "할인율은 1보다 클 수 없습니다.";
    private final Long id;
    private final String name;
    private final Money threshold;
    private final BigDecimal discountRate;

    public DefaultDiscountPolicy(final Long id, final String name, final Money threshold, final BigDecimal discountRate) {
        this.id = id;
        this.name = name;
        this.threshold = threshold;
        validateDiscountRate(discountRate);

        this.discountRate = discountRate;
    }

    public DefaultDiscountPolicy(final String name, final Money threshold, final BigDecimal discountRate) {
        this(null, name, threshold, discountRate);
    }

    private static void validateDiscountRate(final BigDecimal discountRate) {
        if (discountRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(DISCOUNT_UNDER_ZERO_ERROR);
        }

        if (discountRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(DISCOUNT_OVER_ONE_ERROR);
        }
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Money calculateDiscountAmount(final Order order) {
        return order.calculateOriginalTotalPrice()
                .multiply(this.discountRate);
    }

    @Override
    public boolean canApply(final Order order) {
        final Money totalPrice = order.calculateOriginalTotalPrice();
        return totalPrice.isGreaterThanOrEqual(this.threshold);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final DefaultDiscountPolicy that = (DefaultDiscountPolicy) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public Money getThreshold() {
        return this.threshold;
    }

    @Override
    public BigDecimal getDiscountRate() {
        return this.discountRate;
    }
}
