package cart.dto;

import cart.domain.OrderItem;

public class OrderItemDto {
    private final int quantity;
    private final ProductDto product;

    public OrderItemDto(int quantity, ProductDto product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemDto from(OrderItem orderItem) {
        ProductDto productDto = ProductDto.from(orderItem.getOriginalProduct());
        return new OrderItemDto(orderItem.getQuantity(), productDto);
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}
