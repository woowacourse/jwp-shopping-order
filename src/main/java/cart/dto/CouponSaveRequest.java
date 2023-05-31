package cart.dto;

public class CouponSaveRequest {

    private final String name;
    private final String discountPolicyType;
    private final Long discountValue;
    private final Long minimumPrice;

    public CouponSaveRequest(final String name, final String discountPolicyType, final Long discountValue,
                             final Long minimumPrice) {
        this.name = name;
        this.discountPolicyType = discountPolicyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public String getName() {
        return name;
    }

    public String getDiscountPolicyType() {
        return discountPolicyType;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
