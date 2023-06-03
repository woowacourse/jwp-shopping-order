package cart.order.ui.response;

import cart.order.application.dto.OrderDto;
import cart.order.application.dto.OrderedProductDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private LocalDateTime orderedDateTime;
    private List<OrderedProductDto> products;
    private Long totalPrice;

    public OrderResponse(final Long orderId, final LocalDateTime orderedDateTime, final List<OrderedProductDto> products, final Long totalPrice) {
        this.orderId = orderId;
        this.orderedDateTime = orderedDateTime;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse from(final OrderDto orderDto) {
        return new OrderResponse(
                orderDto.getCartOrder().getId(),
                orderDto.getCartOrder().getCreated(),
                orderDto.getProducts(),
                orderDto.getCartOrder().getTotalPrice()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderedDateTime() {
        return orderedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public List<OrderedProductDto> getProducts() {
        return products;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
