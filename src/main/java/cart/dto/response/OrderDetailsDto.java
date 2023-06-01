package cart.dto.response;

import cart.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailsDto {

    private final long quantity;
    private final ProductResponse product;

    public OrderDetailsDto(final long quantity, final ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderDetailsDto from(final OrderItem orderItem) {
        return new OrderDetailsDto(orderItem.getQuantity(), ProductResponse.from(orderItem.getProduct()));
    }

    public static List<OrderDetailsDto> from(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderDetailsDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
