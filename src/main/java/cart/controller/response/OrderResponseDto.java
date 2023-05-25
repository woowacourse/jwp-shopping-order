package cart.controller.response;

import cart.domain.OrderProduct;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private final Long id;
    private final List<OrderProductResponseDto> orderProducts;
    private final LocalDateTime timestamp;
    private final Integer totalPrice;

    public OrderResponseDto(final Long id,
                            final List<OrderProductResponseDto> orderProducts,
                            final LocalDateTime timestamp,
                            final Integer totalPrice) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
    }

    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProducts;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Long getId() {
        return id;
    }

}
