package cart.domain.coupon;

import cart.domain.discount.Policy;
import cart.domain.discount.PolicyDiscount;
import cart.domain.discount.PolicyPercentage;
import cart.exception.CouponCreateBadRequestException;

import java.util.Objects;

public class Coupon {

    private static final String DELIVERY_COUPON = "DELIVERY";
    private static final int MINIMUM_AMOUNT = 1;
    private static final int MAXIMUM_AMOUNT = 100;

    private Long id;
    private final String name;
    private final Policy policy;

    public Coupon(final Long id, final String name, final Policy policy) {
        this.id = id;
        this.name = name;
        this.policy = policy;
    }

    public Coupon(final String name, final Policy policy) {
        this.name = name;
        this.policy = policy;
    }

    public static Coupon create(final String name, final boolean isPercentage, final int amount) {
        validateCoupon(isPercentage, amount);
        return new Coupon(name, createPolicy(isPercentage, amount));
    }

    public static void validateCoupon(final boolean isPercentage, final int amount) {
        if (isPercentage && (amount > MAXIMUM_AMOUNT || amount < MINIMUM_AMOUNT)) {
            throw new CouponCreateBadRequestException();
        }
    }

    public static Policy createPolicy(final boolean isPercentage, final int amount) {
        if (isPercentage) {
            return new PolicyPercentage(amount);
        }

        return new PolicyDiscount(amount);
    }

    public boolean isSame(final Long id) {
        return Objects.equals(this.id, id);
    }

    public static Coupon from(final String name, final boolean isPercentage, final int value) {
        if (isPercentage) {
            return new Coupon(null, name, new PolicyPercentage(value));
        }

        return new Coupon(null, name, new PolicyDiscount(value));
    }

    public int calculate(final int price) {
        return policy.calculate(price);
    }

    public boolean isDeliveryCoupon() {
        return name.startsWith(DELIVERY_COUPON);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Policy getPolicy() {
        return policy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon)) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
