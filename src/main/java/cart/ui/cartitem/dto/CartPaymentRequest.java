package cart.ui.cartitem.dto;

import cart.ui.order.dto.request.CreateOrderItemRequest;

import java.util.List;

public class CartPaymentRequest {

    private List<CreateOrderItemRequest> cartItems;
    private List<Long> couponIds;
    private int usePoint;

    public CartPaymentRequest() {
    }

    public CartPaymentRequest(
            final List<CreateOrderItemRequest> cartItems,
            final List<Long> couponIds,
            final int usePoint
    ) {
        this.cartItems = cartItems;
        this.couponIds = couponIds;
        this.usePoint = usePoint;
    }

    public List<CreateOrderItemRequest> getCartItems() {
        return cartItems;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public int getUsePoint() {
        return usePoint;
    }

}
