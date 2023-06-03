package cart.entity;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final Long discountPrice;
    private final Long minimumPrice;

    public CouponEntity(final String name, final String policyType, final Long discountPrice, final Long minimumPrice) {
        this(null, name, policyType, discountPrice, minimumPrice);
    }

    public CouponEntity(final Long id, final String name, final String policyType, final Long discountPrice,
                        final Long minimumPrice) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountPrice = discountPrice;
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

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
