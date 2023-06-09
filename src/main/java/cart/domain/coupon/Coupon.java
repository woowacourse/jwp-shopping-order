package cart.domain.coupon;

import static cart.exception.badrequest.BadRequestErrorType.COUPON_UNAVAILABLE;
import static cart.exception.badrequest.BadRequestErrorType.DISCOUNT_AMOUNT_INVALID;
import static cart.exception.badrequest.BadRequestErrorType.DISCOUNT_PERCENT_INVALID;

import cart.domain.cart.CartItems;
import cart.exception.badrequest.BadRequestException;
import java.util.Objects;
import java.util.Optional;

public class Coupon {

    private final Long id;
    private final CouponInfo couponInfo;
    private final int value;
    private final CouponType type;

    public Coupon(final CouponInfo couponInfo, final int value, final CouponType type) {
        this(null, couponInfo, value, type);
    }

    public Coupon(final Long id, final CouponInfo couponInfo, final int value, final CouponType type) {
        validate(couponInfo, value, type);
        this.id = id;
        this.couponInfo = couponInfo;
        this.value = value;
        this.type = type;
    }

    public static Coupon of(final Long id, final CouponInfo couponInfo, final Integer discountAmount,
            final Double discountPercentage, final CouponType type) {
        int value = findValue(discountAmount, discountPercentage, type);
        return new Coupon(id, couponInfo, value, type);
    }

    private static Integer findValue(final Integer discountAmount, final Double discountPercentage,
            final CouponType type) {
        if (type == CouponType.FIXED_AMOUNT) {
            if (Objects.isNull(discountAmount)) {
                throw new BadRequestException(DISCOUNT_AMOUNT_INVALID);
            }
            return discountAmount;
        }
        if (Objects.isNull(discountPercentage)) {
            throw new BadRequestException(DISCOUNT_PERCENT_INVALID);
        }
        return (int) (discountPercentage * 100);
    }

    private void validate(final CouponInfo couponInfo, final int value, final CouponType type) {
        type.validateValue(value, couponInfo.getMinOrderPrice());
    }

    public static Coupon none() {
        return new Coupon(CouponInfo.none(), 0, CouponType.NONE);
    }

    public boolean isApplicable(final CartItems cartItems) {
        return couponInfo.getMinOrderPrice() <= cartItems.calculateTotalProductPrice();
    }

    public int calculateDiscountPrice(final CartItems cartItems) {
        if (!isApplicable(cartItems)) {
            throw new BadRequestException(COUPON_UNAVAILABLE);
        }
        return type.calculateDiscountPrice(value, cartItems, couponInfo.getMaxDiscountPrice());
    }

    public Long getId() {
        return id;
    }

    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    public int getValue() {
        return value;
    }

    public CouponType getType() {
        return type;
    }

    public Optional<Integer> getDiscountAmount() {
        if (type == CouponType.FIXED_AMOUNT) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    public Optional<Double> getDiscountPercentage() {
        if (type == CouponType.FIXED_PERCENTAGE) {
            return Optional.of((double) value / 100);
        }
        return Optional.empty();
    }
}
