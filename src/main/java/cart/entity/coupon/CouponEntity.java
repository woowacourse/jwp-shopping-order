package cart.entity.coupon;

public class CouponEntity {

    private final long id;
    private final String name;
    private final long policyId;

    public CouponEntity(final long id, final String name, final long policyId) {
        this.id = id;
        this.name = name;
        this.policyId = policyId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPolicyId() {
        return policyId;
    }
}
