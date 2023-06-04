package cart.dto.order;

import cart.dto.ProductResponse;

public class OrderDetailsDto {
    private Long quantity;
    private ProductResponse product;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(Long quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
