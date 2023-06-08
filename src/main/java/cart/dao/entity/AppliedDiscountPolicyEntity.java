package cart.dao.entity;

public class AppliedDiscountPolicyEntity {
    private final Long id;
    private final Long orderId;
    private final Long discountPolicyId;

    public AppliedDiscountPolicyEntity(Long id, Long orderId, Long discountPolicyId) {
        this.id = id;
        this.orderId = orderId;
        this.discountPolicyId = discountPolicyId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getDiscountPolicyId() {
        return discountPolicyId;
    }
}
