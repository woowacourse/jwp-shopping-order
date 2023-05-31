package cart.dto;

import java.util.List;

public class OrderRequest {
    private List<CartItemOrderRequest> cartItems;
    private Long originalPrice;
    private Long usedPoint;
    private Long pointToAdd;
    
    public OrderRequest() {
    }
    
    public OrderRequest(final List<CartItemOrderRequest> cartItems, final Long originalPrice, final Long usedPoint, final Long pointToAdd) {
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }
    
    public List<CartItemOrderRequest> getCartItems() {
        return cartItems;
    }
    
    public Long getOriginalPrice() {
        return originalPrice;
    }
    
    public Long getUsedPoint() {
        return usedPoint;
    }
    
    public Long getPointToAdd() {
        return pointToAdd;
    }
}
