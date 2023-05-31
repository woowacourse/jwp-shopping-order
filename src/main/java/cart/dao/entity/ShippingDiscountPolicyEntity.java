package cart.dao.entity;

public class ShippingDiscountPolicyEntity {
    Long id;
    Long threshold;

    public ShippingDiscountPolicyEntity(Long id, Long threshold) {
        this.id = id;
        this.threshold = threshold;
    }

    public Long getId() {
        return id;
    }

    public Long getThreshold() {
        return threshold;
    }
}
