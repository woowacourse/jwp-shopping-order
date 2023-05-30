package cart.dto;

import cart.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private long id;
    private String name;
    private String imageUrl;
    private int quantity;
    private long totalPrice;

    public OrderItemResponse(final long id,
                             final String name,
                             final String imageUrl,
                             final int quantity,
                             final long totalPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static List<OrderItemResponse> from(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getName(),
                        orderItem.getImageUrl(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()))
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }
}
