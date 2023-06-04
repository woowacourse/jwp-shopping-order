package cart.domain.discount;

import cart.domain.Grade;

public class GradeDiscount implements Discount {
    private static final String POLICY_NAME = "memberGradeDiscount";
    private static final double GOLD_DISCOUNT_RATE = 0.05;
    private static final double SILVER_DISCOUNT_RATE = 0.03;
    private static final double BRONZE_DISCOUNT_RATE = 0.01;

    private final Grade grade;
    private final int price;

    public GradeDiscount(final Grade grade, final int price) {
        this.grade = grade;
        this.price = price;
    }

    @Override
    public String getName() {
        return POLICY_NAME;
    }

    @Override
    public double getDiscountRate() {
        if (grade == Grade.GOLD) {
            return GOLD_DISCOUNT_RATE;
        }
        if (grade == Grade.SILVER) {
            return SILVER_DISCOUNT_RATE;
        }
        return BRONZE_DISCOUNT_RATE;
    }

    @Override
    public int getDiscountedPrice() {
        return (int) (getDiscountRate() * price);
    }

}
