package cart.domain.shipping;

import cart.dao.entity.ShippingDiscountPolicyEntity;

public class ShippingDiscountPolicy {
    private final Long id;
    private final Long threshold;

    public ShippingDiscountPolicy(Long id, Long threshold) {
        this.id = id;
        this.threshold = threshold;
    }

    public static ShippingDiscountPolicy from(ShippingDiscountPolicyEntity entity) {
        return new ShippingDiscountPolicy(entity.getId(), entity.getThreshold());
    }

    public Long getId() {
        return id;
    }

    public Long getThreshold() {
        return threshold;
    }
}
