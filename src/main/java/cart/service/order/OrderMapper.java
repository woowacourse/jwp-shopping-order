package cart.service.order;

import cart.controller.dto.OrderItemResponse;
import cart.controller.dto.OrderResponse;
import cart.controller.dto.ProductResponse;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class OrderMapper {

    public OrderResponse createOrderResponse(final Order order) {
        final List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::createOrderItem)
                .collect(toList());

        return new OrderResponse(order.getId(), orderItemResponses, Date.from(order.getDate().atZone(ZoneId.systemDefault()).toInstant()), order.getPrice().getPrice());
    }

    public OrderItemResponse createOrderItem(final OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getQuantity().getQuantity(), ProductResponse.from(orderItem));
    }
}
