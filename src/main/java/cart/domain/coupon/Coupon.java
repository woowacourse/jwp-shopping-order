package cart.domain.coupon;

import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.NonDiscountPolicy;
import cart.exception.CouponNotApplyException;
import java.math.BigDecimal;
import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final Long minimumPrice;

    public Coupon(final String name, final DiscountPolicy discountPolicy, final Long minimumPrice) {
        this(null, name, discountPolicy, minimumPrice);
    }

    public Coupon(final Long id, final String name, final DiscountPolicy discountPolicy, final Long minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.minimumPrice = minimumPrice;
    }

    public static Coupon makeNonDiscountPolicyCoupon() {
        return new Coupon(null, "존재하지 않는 쿠폰", new NonDiscountPolicy(), 0L);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public String getType() {
        return this.discountPolicy.getPolicyType().name();
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }

    public BigDecimal apply(final long totalPrice) {
        if (totalPrice > minimumPrice) {
            return discountPolicy.calculatePrice(totalPrice);
        }
        throw new CouponNotApplyException(minimumPrice);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
