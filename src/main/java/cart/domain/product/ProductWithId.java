package cart.domain.product;

public class ProductWithId {

    private final Long productId;
    private final Product product;

    public ProductWithId(final Long productId, final Product product) {
        this.productId = productId;
        this.product = product;
    }

    public boolean isDeleted() {
        return product.isDeleted();
    }

    public Long getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }
}
