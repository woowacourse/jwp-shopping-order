package cart.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final String orderedDateTime;
    private final List<ProductResponse> products;
    private final int totalPrice;

    public OrderResponse(final Long orderId, final LocalDateTime orderedDateTime,
                         final List<ProductResponse> products, final int totalPrice) {
        this.orderId = orderId;
        this.orderedDateTime = convertWithFormat(orderedDateTime);
        this.products = products;
        this.totalPrice = totalPrice;
    }

    private String convertWithFormat(LocalDateTime orderedDateTime) {
        return orderedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderedDateTime() {
        return orderedDateTime;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", orderedDateTime=" + orderedDateTime +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
