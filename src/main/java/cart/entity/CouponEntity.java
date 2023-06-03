package cart.entity;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final Long discountValue;
    private final Long minimumPrice;

    public CouponEntity(final String name, final String policyType, final Long discountValue, final Long minimumPrice) {
        this(null, name, policyType, discountValue, minimumPrice);
    }

    public CouponEntity(final Long id, final String name, final String policyType, final Long discountValue,
                        final Long minimumPrice) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPolicyType() {
        return policyType;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
