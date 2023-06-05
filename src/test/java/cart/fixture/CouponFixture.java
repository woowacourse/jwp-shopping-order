package cart.fixture;

import static cart.domain.coupon.DiscountPolicyType.DELIVERY;
import static cart.domain.coupon.DiscountPolicyType.PERCENT;
import static cart.domain.coupon.DiscountPolicyType.PRICE;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;

@SuppressWarnings("NonAsciiCharacters")
public class CouponFixture {

    public static Coupon 쿠폰_발급(final Coupon coupon, final Long memberId) {
        return new Coupon(null, coupon.getName(), coupon.getDiscountPolicyType(), coupon.getDiscountValue(),
                coupon.getMinimumPrice(), coupon.isUsed(), memberId);
    }

    public static CouponEntity 쿠폰_엔티티_발급(final CouponEntity couponEntity, final Long memberId) {
        return new CouponEntity(
                null,
                couponEntity.getName(),
                couponEntity.getPolicyType(),
                couponEntity.getDiscountValue(),
                couponEntity.getMinimumPrice(),
                couponEntity.isUsed(),
                memberId
        );
    }

    public static final Coupon _3만원_이상_2천원_할인_쿠폰 = new Coupon(
            "30000원 이상 2000원 할인 쿠폰",
            PRICE,
            2000L,
            Money.from(30000L)
    );

    public static final Coupon _3만원_이상_배달비_3천원_할인_쿠폰 = new Coupon(
            "30000원 이상 배달비 할인 쿠폰",
            DELIVERY,
            3000L,
            Money.from(30000L)
    );

    public static final Coupon 배달비_3천원_할인_쿠폰 = new Coupon(
            "배달비 3천원 할인 쿠폰",
            DELIVERY,
            3000L,
            Money.ZERO
    );

    public static final Coupon _20프로_할인_쿠폰 = new Coupon(
            "20% 할인 쿠폰",
            PERCENT,
            20L,
            Money.ZERO
    );

    public static final Coupon _20만원_할인_쿠폰 = new Coupon(
            "20만원 할인 쿠폰",
            PRICE,
            200000L,
            Money.ZERO
    );

    public static final CouponEntity _3만원_이상_3천원_할인_쿠폰_엔티티 = new CouponEntity(
            "30000원 이상 3000원 할인 쿠폰",
            PRICE.name(),
            3000L,
            3000L,
            false,
            null
    );

    public static final CouponEntity _배달비_3천원_할인_쿠폰_엔티티 = new CouponEntity(
            "배달비 3000원 할인 쿠폰",
            DELIVERY.name(),
            3000L,
            0L,
            false,
            null
    );
}
