package cart.dto.response;

import cart.domain.CartItem;

public class ProductQuantityResponse {
    private final ProductResponse product;
    private final int quantity;

    public ProductQuantityResponse(ProductResponse product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static ProductQuantityResponse of(CartItem product) {
        return new ProductQuantityResponse(
                ProductResponse.of(product.getProduct()), product.getQuantity());
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
