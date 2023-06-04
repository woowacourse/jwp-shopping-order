package cart.domain.disount;

public class DiscountPolicyResolver {
    public static DiscountPolicy of(String policyName, int value) {
        if (policyName.equals(FreeDiscountPolicy.NAME)) {
            return new FreeDiscountPolicy();
        }

        if (policyName.equals(PercentDiscountPolicy.NAME)) {
            return new PercentDiscountPolicy(value);
        }

        if (policyName.equals((PriceDiscountPolicy.NAME))) {
            return new PriceDiscountPolicy(value);
        }

        throw new IllegalArgumentException("할인정책을 찾을 수 없습니다.");
    }
}
