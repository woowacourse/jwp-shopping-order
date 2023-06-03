package cart.domain.coupon;

import cart.domain.Money;
import cart.exception.IllegalCouponException;
import java.math.BigDecimal;

public class Coupon {

    public static final Coupon NONE = new Coupon(CouponType.NONE, "NONE", BigDecimal.ZERO, Money.ZERO);

    private final Long id;
    private final String name;
    private final CouponType couponType;
    private final BigDecimal discountValue;
    private final Money minOrderPrice;

    public Coupon(CouponType couponType, String name, BigDecimal discountValue, Money minOrderPrice) {
        this(null, name, couponType, discountValue, minOrderPrice);
    }

    public Coupon(Long id, String name, CouponType couponType, BigDecimal discountValue, Money minOrderPrice) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
        validateDiscountValue(couponType, discountValue);
        this.discountValue = discountValue;
        this.minOrderPrice = minOrderPrice;
    }

    private void validateDiscountValue(CouponType couponType, BigDecimal discountValue) {
        if (!couponType.isValid(discountValue)) {
            throw new IllegalCouponException();
        }
    }

    public Money discountPrice(Money totalCartsPrice) {
        if (totalCartsPrice.isLessThan(minOrderPrice.getValue())) {
            throw new IllegalCouponException();
        }
        return couponType.discount(totalCartsPrice, discountValue);
    }

    public boolean isCoupon() {
        return couponType != CouponType.NONE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public Money getMinOrderPrice() {
        return minOrderPrice;
    }
}
