package cart.sale;

public class Sale {
    private final String name;
    private final Long discountPolicyId;

    public Sale(String name, Long discountPolicyId) {
        this.name = name;
        this.discountPolicyId = discountPolicyId;
    }

    public Long getDiscountPolicyId() {
        return discountPolicyId;
    }
}
