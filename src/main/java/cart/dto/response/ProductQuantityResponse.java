package cart.dto.response;

import cart.domain.CartItem;

public class ProductQuantityResponse {
    private final ProductResponse product;
    private final int quantity;

    private ProductQuantityResponse(ProductResponse product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static ProductQuantityResponse from(CartItem product) {
        return new ProductQuantityResponse(
                ProductResponse.from(product.getProduct()), product.getQuantity());
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
