package cart.dto.payment;

import cart.domain.cart.CartItem;

public class PaymentUsingCouponResponse {

    private final long productId;
    private final int originPrice;
    private final int discountPrice;

    public PaymentUsingCouponResponse(final long productId, final int originPrice, final int discountPrice) {
        this.productId = productId;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
    }

    public static PaymentUsingCouponResponse from(final CartItem cartItem) {
        return new PaymentUsingCouponResponse(cartItem.getProduct().getId(), cartItem.getOriginPrice(), cartItem.getOriginPrice() - cartItem.getApplyDiscountPrice());
    }

    public long getProductId() {
        return productId;
    }

    public int getOriginPrice() {
        return originPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
