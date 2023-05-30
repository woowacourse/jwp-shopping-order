package cart.dto;

import cart.domain.Coupon;
import cart.domain.Orders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdersDetailResponse {
    private long id;
    private List<CartItemResponse> orderProducts;
    private int originalPrice;
    private int discountPrice;
    private boolean confirmState;
    private CouponResponse coupon;

    private OrdersDetailResponse() {

    }

    public OrdersDetailResponse(long id, List<CartItemResponse> orderProducts, int originalPrice, int discountPrice, boolean confirmState, CouponResponse coupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    public static OrdersDetailResponse of(Orders orders) {
        Optional<Coupon> coupon = Optional.ofNullable(orders.getCoupons().get(0));
        return new OrdersDetailResponse(
                orders.getId(),
                orders.getCartItems().stream().map(CartItemResponse::of).collect(Collectors.toList()),
                orders.getOriginalPrice(),
                orders.getDiscountPrice(),
                orders.isConfirmState(),
                CouponResponse.of(coupon)
        );
    }

    public long getId() {
        return id;
    }

    public List<CartItemResponse> getOrderProducts() {
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
