package cart.mapper;

import cart.domain.Order;
import cart.dto.response.OrderProductResponse;
import cart.dto.response.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderProductResponse> detailResponse = order.getOrderDetails().stream()
                .map(OrderDetailMapper::toResponse)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), detailResponse, order.getCreatedAt());
    }
}
