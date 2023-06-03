package cart.domain.product.dto;

import cart.domain.product.Product;

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
