package cart.dto.response;

import cart.domain.OrderEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderResponse {

    private final Long orderId;
    private final String orderDateTime;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;

    public static OrderResponse of(final OrderEntity orderEntity, final List<OrderItemResponse> orderItemResponses) {
        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getCreatedAt(),
                orderItemResponses,
                orderEntity.getTotalPrice()
        );
    }
}
