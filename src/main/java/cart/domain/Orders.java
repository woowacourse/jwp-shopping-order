package cart.domain;

import java.util.List;

public class Orders {
    private Long memberId;
    private List<Long> cartId;
    private int originalPrice;
    private int discountPrice;
    private Long couponId;

    public Orders(Long memberId, List<Long> cartId, int originalPrice, int discountPrice, Long couponId) {
        this.memberId = memberId;
        this.cartId = cartId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.couponId = couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<Long> getCartId() {
        return cartId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public Long getCouponId() {
        return couponId;
    }
}
