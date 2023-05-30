package cart.order.presentation;

import cart.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {
    private long productId;
    private String productName;
    private int price;

    public OrderItemResponse() {
    }

    public OrderItemResponse(long productId, String productName, int price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public static List<OrderItemResponse> from(List<OrderItem> orderItems) {
        return orderItems
                .stream().map(orderItem -> new OrderItemResponse(orderItem.getProductId(), orderItem.getProductName(), orderItem.getPrice()))
                .collect(Collectors.toList());
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }
}
