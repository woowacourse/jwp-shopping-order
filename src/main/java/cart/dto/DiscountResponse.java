package cart.dto;

public class DiscountResponse {
    private final String discountPolicy;
    private final int discountAmount;

    public DiscountResponse(String discountPolicy, int discountAmount) {
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
