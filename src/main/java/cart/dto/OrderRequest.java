package cart.dto;

import java.util.List;

public class OrderRequest {

    private final List<Long> cartItemIds;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

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
