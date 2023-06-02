package cart.order.dto;

import cart.cartitem.dto.CartItemOrderRequest;

import java.util.List;

public class OrderRequest {
    private List<CartItemOrderRequest> cartItemIds;
    private Long originalPrice;
    private Long usedPoint;
    private Long pointToAdd;
    
    public OrderRequest() {
    }
    
    public OrderRequest(final List<CartItemOrderRequest> cartItemIds, final Long originalPrice, final Long usedPoint, final Long pointToAdd) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }
    
    public List<CartItemOrderRequest> getCartItemIds() {
        return cartItemIds;
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
    
    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartItems=" + cartItemIds +
                ", originalPrice=" + originalPrice +
                ", usedPoint=" + usedPoint +
                ", pointToAdd=" + pointToAdd +
                '}';
    }
}
