package cart.step2.order.presentation.dto;

import cart.step2.order.domain.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime date;
    private int price;

    public OrderResponse(final Long id, final List<OrderItem> orderItems, final LocalDateTime date, final int price) {
        this.id = id;
        this.orderItems = orderItems.stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        this.date = date;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

}
