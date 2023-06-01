package cart.dto.order;

import cart.dto.CouponResponse;

import java.util.List;

public class OrderDetailResponse {
    private final Long id;
    private final List<OrderProductResponse> orderProducts;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final Boolean confirmState;
    private final CouponResponse coupon;

    public OrderDetailResponse(Long id, List<OrderProductResponse> orderProducts, Integer originalPrice, Integer discountPrice, Boolean confirmState, CouponResponse coupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    public Long getId() {
        return id;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Boolean getConfirmState() {
        return confirmState;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}
