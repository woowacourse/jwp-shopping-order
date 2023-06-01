package cart.entity.product;

public class ProductSaleEntity {

    private final Long id;
    private final Long productId;
    private final Long policyId;

    public ProductSaleEntity(final Long id, final Long productId, final Long policyId) {
        this.id = id;
        this.productId = productId;
        this.policyId = policyId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPolicyId() {
        return policyId;
    }
}
