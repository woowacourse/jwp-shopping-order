package cart.dao.entity;

public class DefaultDiscountPolicyEntity {
    private final long id;
    private final String name;
    private final int threshold;
    private final double discountRate;

    public DefaultDiscountPolicyEntity(final long id, final String name, final int threshold, final double discountRate) {
        this.id = id;
        this.name = name;
        this.threshold = threshold;
        this.discountRate = discountRate;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public double getDiscountRate() {
        return this.discountRate;
    }
}
