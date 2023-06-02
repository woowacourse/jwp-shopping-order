package cart.presentation.dto.request;

import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;
    private Long originalPrice;
    private Long usedPoint;
    private Long pointToAdd;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public List<Long> getCartItemIds() {
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
}
