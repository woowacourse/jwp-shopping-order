package cart.dao.entity;

public class DefaultDeliveryPolicyEntity {
    private final Long id;
    private final String name;
    private final int fee;

    public DefaultDeliveryPolicyEntity(final Long id, final String name, final int fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getFee() {
        return this.fee;
    }
}
