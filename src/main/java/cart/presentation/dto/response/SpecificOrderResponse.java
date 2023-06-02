package cart.presentation.dto.response;

import java.util.List;

public class SpecificOrderResponse {

    private final Long orderId;
    private final List<OrderDto> orderDto;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

    public SpecificOrderResponse(Long orderId, List<OrderDto> orderDto,
                                 Long originalPrice, Long usedPoint, Long pointToAdd) {
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
