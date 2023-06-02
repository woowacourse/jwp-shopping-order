package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.Money;
import cart.exception.IllegalCouponException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Coupon {

    private static final int DEFAULT_EXPIRED_DATE_PERIOD = 3;

    private final Long id;
    private final Member member;
    private final CouponType couponType;
    private final BigDecimal discountValue;
    private final LocalDate expiredDate;
    private final Money minOrderPrice;

    public Coupon(Member member, CouponType couponType, BigDecimal discountValue, Money minOrderPrice) {
        this(null, member, couponType, discountValue, makDefaultExpiredDate(), minOrderPrice);
    }

    public Coupon(Member member, CouponType couponType, BigDecimal discountValue, LocalDate expiredDate,
                  Money minOrderPrice) {
        this(null, member, couponType, discountValue, expiredDate, minOrderPrice);
    }

    public Coupon(Long id, Member member, CouponType couponType, BigDecimal discountValue, LocalDate expiredDate,
                  Money minOrderPrice) {
        this.id = id;
        this.member = member;
        this.couponType = couponType;
        validateDiscountValue(couponType, discountValue);
        this.discountValue = discountValue;
        this.expiredDate = expiredDate;
        this.minOrderPrice = minOrderPrice;
    }

    private static LocalDate makDefaultExpiredDate() {
        return LocalDate.now().plusDays(DEFAULT_EXPIRED_DATE_PERIOD);
    }

    private void validateDiscountValue(CouponType couponType, BigDecimal discountValue) {
        if (!couponType.isValid(discountValue)) {
            throw new IllegalCouponException();
        }
    }

    public Money discountPrice(Money totalCartsPrice) {
        if (expiredDate.isBefore(LocalDate.now())) {
            throw new IllegalCouponException();
        }
        if (totalCartsPrice.isLessThan(minOrderPrice.getValue())) {
            throw new IllegalCouponException();
        }
        return couponType.discount(totalCartsPrice, discountValue);
    }
}
