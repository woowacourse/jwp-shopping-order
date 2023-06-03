package cart.dto.response;

import java.util.List;

public class OrderResponse {
    private final Long id;
    private final List<ProductQuantityResponse> orderProducts;
    private final int originalPrice;
    private final int discountPrice;
    private final boolean confirmState;
    private final CouponResponse coupon;

    public OrderResponse(Long id, List<ProductQuantityResponse> products, int originalPrice, int discountPrice, boolean confirmState, CouponResponse couponresponse) {
        this.id = id;
        this.orderProducts = products;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = couponresponse;
    }

    public Long getId() {
        return id;
    }

    public List<ProductQuantityResponse> getOrderProducts() {
        return orderProducts;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public boolean isConfirmState() {
        return confirmState;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}
