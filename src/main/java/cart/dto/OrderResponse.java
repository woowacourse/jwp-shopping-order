package cart.dto;

public class OrderResponse {

    private final long orderId;
    private final int totalPrice;
    private final CartItemResponse cartItemResponse;

    public OrderResponse(final long orderId, final int totalPrice, final CartItemResponse cartItemResponse) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.cartItemResponse = cartItemResponse;
    }

    public long getOrderId() {
        return orderId;
    }

    public CartItemResponse getCartItemResponse() {
        return cartItemResponse;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
