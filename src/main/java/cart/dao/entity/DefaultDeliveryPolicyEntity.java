package cart.dao.entity;

public class DefaultDeliveryPolicyEntity {
    private final long id;
    private final String name;
    private final int fee;

    public DefaultDeliveryPolicyEntity(final long id, final String name, final int fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getFee() {
        return this.fee;
    }
}
