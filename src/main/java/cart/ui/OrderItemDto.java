package cart.ui;

import cart.dto.ProductDto;

public class OrderItemDto {
    private final int quantity;
    private final ProductDto product;

    public OrderItemDto(int quantity, ProductDto product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}
