package cart.dto;

import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Orders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdersResponse {
    private Long id;
    private List<CartItemResponse> ordersProduct;
    private int originalPrice;
    private int discountPrice;
    private boolean confirmState;
    private CouponResponse coupon;
    private OrdersResponse(){

    }
    private OrdersResponse(CouponResponse coupon){
        this.coupon = coupon;
    }
    private OrdersResponse(Long id, List<CartItemResponse> ordersProduct, boolean confirmState) {
        this.id = id;
        this.ordersProduct = ordersProduct;
        this.confirmState = confirmState;
    }

    private OrdersResponse(Long id, List<CartItemResponse> ordersProduct, int originalPrice, int discountPrice, boolean confirmState, CouponResponse coupon) {
        this.id = id;
        this.ordersProduct = ordersProduct;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    public static OrdersResponse of(Orders orders){
        return new OrdersResponse(
                orders.getId(),
                orders.getCartItems().stream().map(CartItemResponse::of).collect(Collectors.toList()),
                orders.isConfirmState()
        );
    }

    public static OrdersResponse ofDetail(Orders orders){
        return new OrdersResponse(
                orders.getId(),
                orders.getCartItems().stream().map(CartItemResponse::of).collect(Collectors.toList()),
                orders.getOriginalPrice(),
                orders.getDiscountPrice(),
                orders.isConfirmState(),
                CouponResponse.of(Optional.of(orders.getCoupons().get(0)))
        );
    }
    public static OrdersResponse ofCoupon(Coupon coupon){
        return new OrdersResponse(
                CouponResponse.of(Optional.ofNullable(coupon))
        );
    }
    public List<CartItemResponse> getOrdersProduct() {
        return ordersProduct;
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
