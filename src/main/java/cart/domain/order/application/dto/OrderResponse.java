package cart.domain.order.application.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import cart.domain.order.domain.dto.OrderedProductDto;

public class OrderResponse {

    private final Long orderId;
    private final String orderedDateTime;
    private final List<OrderedProductDto> products;
    private final int totalPrice;

    public OrderResponse(final Long orderId, final LocalDateTime orderedDateTime,
                         final List<OrderedProductDto> products, final int totalPrice) {
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

    public List<OrderedProductDto> getProducts() {
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
