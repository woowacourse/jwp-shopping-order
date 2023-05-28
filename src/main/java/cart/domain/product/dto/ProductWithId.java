package cart.domain.product.dto;

import cart.domain.product.Product;

public class ProductWithId {
    private final Long id;
    private final Product product;

    public ProductWithId(final Long id, final Product product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }
}
