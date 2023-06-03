package cart.dto;

import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Orders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdersResponse {
    private Long id;
    private List<CartItemResponse> orderProducts;
    private int originalPrice;
    private int discountPrice;
    private boolean confirmState;
    private CouponResponse coupon;

    private OrdersResponse() {

    }

    public static OrdersResponse noOrdersMembersResponse() {
        return new OrdersResponse();
    }

    private OrdersResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    private OrdersResponse(Long id, List<CartItemResponse> orderProducts, boolean confirmState) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.confirmState = confirmState;
    }

    private OrdersResponse(Long id, List<CartItemResponse> orderProducts, int originalPrice, int discountPrice, boolean confirmState, CouponResponse coupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    private OrdersResponse(Long id, List<CartItemResponse> orderProducts, int originalPrice, int discountPrice, boolean confirmState) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
    }

    public static OrdersResponse of(Orders orders, List<CartItem> ordersProduct, int originalPrice, final List<Coupon> coupons) {
        if (coupons.isEmpty()) {
            return new OrdersResponse(
                    orders.getId(),
                    ordersProduct.stream().map(CartItemResponse::of).collect(Collectors.toList()),
                    originalPrice,
                    orders.getPrice(),
                    orders.isConfirmState()
            );
        }
        return new OrdersResponse(
                orders.getId(),
                ordersProduct.stream().map(CartItemResponse::of).collect(Collectors.toList()),
                originalPrice,
                orders.getPrice(),
                orders.isConfirmState(),
                CouponResponse.of(Optional.of(coupons.get(0)))
        );
    }

    public static OrdersResponse ofCoupon(Coupon coupon) {
        return new OrdersResponse(
                CouponResponse.of(Optional.ofNullable(coupon))
        );
    }

    public List<CartItemResponse> getOrderProducts() {
        return orderProducts;
    }

    public Long getId() {
        return id;
    }


    public boolean isConfirmState() {
        return confirmState;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}
