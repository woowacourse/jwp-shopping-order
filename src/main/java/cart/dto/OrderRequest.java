package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<Long> order;
    private Integer originalPrice;
    private Integer usedPoint;
    private Integer pointToAdd;

    public OrderRequest() {
    }

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
