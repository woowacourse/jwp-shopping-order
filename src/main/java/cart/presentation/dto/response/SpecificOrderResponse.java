package cart.presentation.dto.response;

import java.util.List;

public class SpecificOrderResponse {

    private final Long orderId;
    private final List<OrderInfo> orderInfo;
    private final Integer originalPrice;
    private final Integer usedPoint;
    private final Integer pointToAdd;

    public SpecificOrderResponse(Long orderId, List<OrderInfo> orderInfo,
                                 Integer originalPrice, Integer usedPoint, Integer pointToAdd) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderInfo> getOrderInfo() {
        return orderInfo;
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
