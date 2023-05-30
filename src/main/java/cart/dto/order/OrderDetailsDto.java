package cart.dto.order;

import cart.dto.ProductResponse;

public class OrderDetailsDto {

    private Long quantity;
    private ProductResponse productResponse;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(Long quantity, ProductResponse productResponse) {
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }
}
