package cart.dto;

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
}
