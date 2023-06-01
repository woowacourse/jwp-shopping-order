package cart.dto.response;

import cart.domain.MemberCoupon;
import cart.domain.Money;

public class MemberCouponResponse {
    private final Long id;
    private final String name;
    private final int minOrderPrice;
    private final int maxDiscountPrice;
    private final boolean isAvailable;
    private final int discountPrice;
    private final String expiredAt;

    public MemberCouponResponse(MemberCoupon memberCoupon, Money price) {
        this.id = memberCoupon.getId();
        this.name = memberCoupon.getCoupon().getName();
        this.minOrderPrice = memberCoupon.getCoupon().getMinOrderPrice();
        this.maxDiscountPrice = memberCoupon.getCoupon().getMaxDiscountPrice();
        this.isAvailable = memberCoupon.isAvailable(price);
        this.discountPrice = price.value() - memberCoupon.getCoupon().discount(price).value();
        this.expiredAt = memberCoupon.getExpiredAt();
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

    public String getExpiredAt() {
        return expiredAt;
    }
}
