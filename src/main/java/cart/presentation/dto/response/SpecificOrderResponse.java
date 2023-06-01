package cart.presentation.dto.response;

import java.util.List;

public class SpecificOrderResponse {

    private final Long orderId;
    private final List<OrderDto> orderDto;
    private final Integer originalPrice;
    private final Integer usedPoint;
    private final Integer pointToAdd;

    public SpecificOrderResponse(Long orderId, List<OrderDto> orderDto,
                                 Integer originalPrice, Integer usedPoint, Integer pointToAdd) {
        this.orderId = orderId;
        this.orderDto = orderDto;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderDto> getOrderInfo() {
        return orderDto;
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
