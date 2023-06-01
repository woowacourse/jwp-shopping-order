package cart.dto;

import cart.domain.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private String orderNumber;
    private LocalDate date;
    private List<OrderProductResponse> products;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String orderNumber, LocalDate date, List<OrderProductResponse> products) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.date = date;
        this.products = products;
    }

    public static OrderResponse from(Order order) {
        List<OrderProductResponse> products = order.getItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getOrderDate().toLocalDate(), products);
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }
}
