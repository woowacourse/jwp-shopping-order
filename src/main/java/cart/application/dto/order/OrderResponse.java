package cart.application.dto.order;

import cart.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private long id;
    private List<OrderProductResponse> products;

    private OrderResponse() {
    }

    public OrderResponse(final long id, final List<OrderProductResponse> products) {
        this.id = id;
        this.products = products;
    }

    public static OrderResponse from(final Order order) {
        List<OrderProductResponse> products = order.getOrderItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), products);
    }

    public long getId() {
        return id;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }
}
