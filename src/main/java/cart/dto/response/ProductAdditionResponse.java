package cart.dto.response;

public class ProductAdditionResponse {

    private long productId;

    public ProductAdditionResponse(final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
