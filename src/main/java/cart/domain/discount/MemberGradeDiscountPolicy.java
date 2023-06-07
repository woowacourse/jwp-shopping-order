package cart.domain.discount;

import cart.domain.MemberGrade;

import static cart.domain.MemberGrade.*;

public final class MemberGradeDiscountPolicy implements DiscountPolicy {
    private static final String NAME = "memberGradeDiscount";

    private static final double GOLD_DISCOUNT_RATE = 0.05;
    private static final double SILVER_DISCOUNT_RATE = 0.03;
    private static final double BRONZE_DISCOUNT_RATE = 0.01;

    private final MemberGrade memberGrade;

    public MemberGradeDiscountPolicy(final MemberGrade memberGrade) {
        this.memberGrade = memberGrade;
    }

    @Override
    public int calculateDiscountAmount(final int price) {
        if (GOLD.equals(memberGrade)) {
            return (int) (price * GOLD_DISCOUNT_RATE);
        }
        if (SILVER.equals(memberGrade)) {
            return (int) (price * SILVER_DISCOUNT_RATE);
        }
        if (BRONZE.equals(memberGrade)) {
            return (int) (price * BRONZE_DISCOUNT_RATE);
        }

        return 0;
    }

    public double getRate() {
        if (GOLD.equals(memberGrade)) {
            return GOLD_DISCOUNT_RATE;
        }
        if (SILVER.equals(memberGrade)) {
            return SILVER_DISCOUNT_RATE;
        }
        if (BRONZE.equals(memberGrade)) {
            return BRONZE_DISCOUNT_RATE;
        }

        return 0;
    }

    public String getName() {
        return NAME;
    }
}
