package cart.application.dto;

public class SingleKindDetailedProductResponse {

    private final Integer quantity;

    private final ProductResponse product;

    public SingleKindDetailedProductResponse(Integer quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
