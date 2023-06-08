package cart.order.application.dto;

import cart.order.domain.OrderItem;
import cart.order.presentation.request.ProductInOrderItemDto;

public class OrderItemDto {
    private final int quantity;
    private final ProductInOrderItemDto product;

    public OrderItemDto(int quantity, ProductInOrderItemDto product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemDto from(OrderItem orderItem) {
        ProductInOrderItemDto productInCartItemDto = ProductInOrderItemDto.from(orderItem.getOriginalProduct());
        return new OrderItemDto(orderItem.getQuantity(), productInCartItemDto);
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductInOrderItemDto getProduct() {
        return product;
    }
}
