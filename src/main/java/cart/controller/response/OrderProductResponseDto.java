package cart.controller.response;

import cart.dto.ProductResponse;

public class OrderProductResponseDto {

    private final ProductResponse productResponse;
    private final Integer quantity;

    public OrderProductResponseDto(final ProductResponse productResponse, final Integer quantity) {
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
