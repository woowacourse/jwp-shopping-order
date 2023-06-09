package cart.fixtures;

import cart.domain.Coupon;
import cart.dto.coupon.CouponResponse;

import java.time.LocalDateTime;

public class CouponFixtures {

    public static final Coupon COUPON1 =
            new Coupon(1L, "1000원 할인 쿠폰", 1000, 0,
                    LocalDateTime.of(2023, 12, 31, 23, 59, 59));
    public static final Coupon COUPON2 =
            new Coupon(2L, "2000원 할인 쿠폰", 2000, 0,
                    LocalDateTime.of(2023, 12, 31, 23, 59, 59));
    public static final Coupon COUPON3 =
            new Coupon(3L, "3000원 할인 쿠폰", 3000, 0,
                    LocalDateTime.of(2023, 12, 31, 23, 59, 59));

    public static final Coupon NEW_COUPON_TO_INSERT =
            new Coupon(null, "새쿠폰", 5000, 0,
                    LocalDateTime.of(2023, 12, 31, 23, 59, 59));
    public static final Coupon NEW_COUPON =
            new Coupon(4L, "새쿠폰", 5000, 0,
                    LocalDateTime.of(2023, 12, 31, 23, 59, 59));

    public static final Coupon BONUS_COUPON =
            new Coupon(null, "5천원 할인 쿠폰", 5000, 0, LocalDateTime.now().plusDays(7));

    public static final CouponResponse COUPON1_RESPONSE = CouponResponse.of(COUPON1);
    public static final CouponResponse COUPON2_RESPONSE = CouponResponse.of(COUPON2);
    public static final CouponResponse COUPON3_RESPONSE = CouponResponse.of(COUPON3);
}
