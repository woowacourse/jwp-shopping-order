package cart.controller.dto;

import java.util.Date;
import java.util.List;

public class OrderResponse {

    private final Long id;
    private final List<OrderItemResponse> orderItems;
    private final Date date;
    private final int price;

    public OrderResponse(final Long id, final List<OrderItemResponse> orderItems, final Date date, final int price) {
        this.id = id;
        this.orderItems = orderItems;
        this.date = date;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
