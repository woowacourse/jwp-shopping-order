package cart.coupon;

public class Coupon {
    private final Long id;
    private final String name;
    private final Long discountConditionId;

    public Coupon(Long id, String name, Long discountConditionId) {
        this.id = id;
        this.name = name;
        this.discountConditionId = discountConditionId;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
