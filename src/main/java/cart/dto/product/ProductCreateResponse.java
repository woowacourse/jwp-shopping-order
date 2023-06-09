package cart.dto.product;

public class ProductCreateResponse {
    private Long productId;

    public ProductCreateResponse() {
    }

    public ProductCreateResponse(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
