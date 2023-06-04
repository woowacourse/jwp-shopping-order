package cart.dto.response;

import java.time.LocalDateTime;

import cart.domain.MemberCoupon;
import cart.domain.Money;

public class MemberCouponResponse {
    private final Long id;
    private final String name;
    private final Integer minOrderPrice;
    private final Integer maxDiscountPrice;
    private final boolean isAvailable;
    private final Integer discountPrice;
    private final LocalDateTime expiredAt;

    private MemberCouponResponse(
        Long id,
        String name,
        Integer minOrderPrice,
        Integer maxDiscountPrice,
        boolean isAvailable,
        Integer discountPrice,
        LocalDateTime expiredAt
    ) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.isAvailable = isAvailable;
        this.discountPrice = discountPrice;
        this.expiredAt = expiredAt;
    }

    public static MemberCouponResponse of(MemberCoupon memberCoupon, Money price) {
        return new MemberCouponResponse(
            memberCoupon.getId(),
            memberCoupon.getCoupon().getName(),
            memberCoupon.getCoupon().getMinOrderPrice(),
            memberCoupon.getCoupon().getMaxDiscountPrice(),
            memberCoupon.isAvailable(price),
            price.value() - memberCoupon.getCoupon().discount(price).value(),
            memberCoupon.getExpiredAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinOrderPrice() {
        return minOrderPrice;
    }

    public int getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
