package cart.dto;

public class DefaultDeliveryPolicyResponse {
    private final Long id;
    private final String name;
    private final int fee;

    public DefaultDeliveryPolicyResponse(final Long id, final String name, final int fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    public static DefaultDeliveryPolicyResponse from(final cart.domain.DeliveryPolicy defaultDeliveryPolicy) {
        return new DefaultDeliveryPolicyResponse(defaultDeliveryPolicy.getId(), defaultDeliveryPolicy.getName(), defaultDeliveryPolicy.getFee().getValue());
    }

    public String getName() {
        return this.name;
    }

    public int getFee() {
        return this.fee;
    }

    public Long getId() {
        return this.id;
    }
}
