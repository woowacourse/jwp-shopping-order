package cart.presentation.presentation;

import cart.order.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private String orderedTime;
    private List<OrderItemResponse> orderedItems;

    public OrderResponse(Long id, String orderedTime, List<OrderItemResponse> orderedItems) {
        this.id = id;
        this.orderedTime = orderedTime;
        this.orderedItems = orderedItems;
    }

    public static OrderResponse from(Order order) {
        final var orderedItems = order.getOrderItems()
                .stream().map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderedTime()),
                orderedItems
        );
    }

    public Long getId() {
        return id;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public List<OrderItemResponse> getOrderedItems() {
        return orderedItems;
    }
}
