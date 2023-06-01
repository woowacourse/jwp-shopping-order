package cart.dto;

public class CartItemOrderRequest {
    private Long cartItemId;
    
    public CartItemOrderRequest() {
    }
    
    public CartItemOrderRequest(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public Long getCartItemId() {
        return cartItemId;
    }
    
    @Override
    public String toString() {
        return "CartItemOrderRequest{" +
                "cartItemId=" + cartItemId +
                '}';
    }
}
