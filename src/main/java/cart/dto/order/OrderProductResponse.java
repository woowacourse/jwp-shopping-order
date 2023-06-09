package cart.dto.order;

import cart.dto.ProductResponse;

public class OrderProductResponse {
    private final ProductResponse product;
    private final Integer quantity;

    public OrderProductResponse(ProductResponse product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
