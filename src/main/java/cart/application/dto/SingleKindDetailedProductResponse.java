package cart.application.dto;

import cart.domain.QuantityAndProduct;

public class SingleKindDetailedProductResponse {

    private final Integer quantity;

    private final ProductResponse product;

    public SingleKindDetailedProductResponse(Integer quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static SingleKindDetailedProductResponse of(QuantityAndProduct quantityAndProduct) {
        return new SingleKindDetailedProductResponse(quantityAndProduct.getQuantity(),
            ProductResponse.of(quantityAndProduct.getProduct()));
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
