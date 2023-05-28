package cart.dto.product;

import cart.domain.member.Member;

public class DeliveryPayResponse {

    private final int originalPrice;
    private final int discountPrice;

    public DeliveryPayResponse(final int originalPrice, final int discountPrice) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public static DeliveryPayResponse from(final Member member) {
        return new DeliveryPayResponse(member.getCart().getDeliveryFee(), member.getCart().calculateDeliveryFeeUsingCoupons(member.getCoupons()));
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
