package cart.dao.entity;

public class AppliedDeliveryPolicyEntity {
    private final Long id;
    private final Long orderId;
    private final Long deliveryPolicyId;

    public AppliedDeliveryPolicyEntity(Long id, Long orderId, Long deliveryPolicyId) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryPolicyId = deliveryPolicyId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getDeliveryPolicyId() {
        return deliveryPolicyId;
    }
}
