package cart.dto;

import java.util.List;

public class OrderRequest {

    private final List<Long> order;
    private final Integer originalPrice;
    private final Integer usedPoint;
    private final Integer pointToAdd;

    public OrderRequest(List<Long> order, Integer originalPrice, Integer usedPoint, Integer pointToAdd) {
        this.order = order;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public List<Long> getOrder() {
        return order;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Integer getPointToAdd() {
        return pointToAdd;
    }
}
