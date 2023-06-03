package cart.domain.discount;

import cart.domain.Grade;

public class GradeDiscount implements Discount {
    private static final String POLICY_NAME = "memberGradeDiscount";

    private final double rate;
    private final int price;

    private GradeDiscount(final double rate, final int price) {
        this.rate = rate;
        this.price = price;
    }

    public static GradeDiscount of(final Grade grade, final int price) {
        return new GradeDiscount(grade.getDiscountRate(), price);
    }

    @Override
    public String getName() {
        return POLICY_NAME;
    }

    @Override
    public double getRate() {
        return rate;
    }

    @Override
    public int getMoney() {
        return (int) (rate * price);
    }

}
