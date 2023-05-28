package cart.entity.policy;

public class PolicyEntity {

    private final Long id;
    private final boolean isPercentage;
    private final int amount;

    public PolicyEntity(final Long id, final boolean isPercentage, final int amount) {
        this.id = id;
        this.isPercentage = isPercentage;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public boolean isOnSale() {
        return amount != 0;
    }

    public int getAmount() {
        return amount;
    }
}
