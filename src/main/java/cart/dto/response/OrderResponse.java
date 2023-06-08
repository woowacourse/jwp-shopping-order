package cart.dto.response;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final List<ProductQuantityResponse> orderProducts;
    private final int originalPrice;
    private final int discountPrice;
    private final boolean confirmState;
    private final CouponResponse coupon;

    private OrderResponse(Long id, List<ProductQuantityResponse> products, int originalPrice, int discountPrice, boolean confirmState, CouponResponse couponresponse) {
        this.id = id;
        this.orderProducts = products;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = couponresponse;
    }

    public static OrderResponse of(Order order) {
        List<ProductQuantityResponse> products = order.getCartProducts().stream()
                .map(ProductQuantityResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), products, order.calculatePrice(), order.calculateDiscountPrice(),
                order.getConfirmState(), CouponResponse.from(order.getCoupon()));
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
