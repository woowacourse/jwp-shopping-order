package cart.order.ui.dto;

import cart.cartItem.ui.dto.ProductInCartItemDto;
import cart.order.domain.OrderItem;

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
