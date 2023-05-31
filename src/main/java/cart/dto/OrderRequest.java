package cart.dto;

import java.util.List;

public class OrderRequest {

    private final List<Long> order;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

    public OrderRequest(List<Long> order, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.order = order;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public List<Long> getOrder() {
        return order;
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
