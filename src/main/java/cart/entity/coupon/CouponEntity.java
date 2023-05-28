package cart.entity.coupon;

public class CouponEntity {

    private final long id;
    private final String name;
    private final long policyId;
    private final long memberId;

    public CouponEntity(final long id, final String name, final long policyId, final long memberId) {
        this.id = id;
        this.name = name;
        this.policyId = policyId;
        this.memberId = memberId;
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

    public long getMemberId() {
        return memberId;
    }
}
