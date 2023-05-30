package cart.domain.policy;

public class DiscountPolicyResolver {
    public static DiscountPolicy of(String policyName, int value) {
        if (policyName.equals(FreePolicy.NAME)) {
            return new FreePolicy();
        }

        if (policyName.equals(PercentPolicy.NAME)) {
            return new PercentPolicy(value);
        }

        if (policyName.equals((PricePolicy.NAME))) {
            return new PricePolicy(value);
        }

        throw new IllegalArgumentException("할인정책을 찾을 수 없습니다.");
    }
}
