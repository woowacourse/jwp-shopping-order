package cart.application.dto.order;

import cart.application.dto.product.ProductResponse;

public class OrderProductResponse {

    private final int quantity;
    private final ProductResponse product;

    public OrderProductResponse(final int quantity, final ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
