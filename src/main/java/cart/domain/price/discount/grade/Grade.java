package cart.domain.price.discount.grade;

import java.util.Arrays;

import cart.exception.InvalidGradeException;

public enum Grade {
    FIVE_PERCENT_DISCOUNT_GRADE(1, 0.05, "gold"),
    THREE_PERCENT_DISCOUNT_GRADE(2, 0.03, "silver"),
    ONE_PERCENT_DISOCUNT_GRADE(3, 0.01, "bronze");

    private final Integer gradeValue;
    private final Double discountRate;
    private final String gradeName;

    Grade(Integer gradeValue, Double discountRate, String gradeName) {
        this.gradeValue = gradeValue;
        this.discountRate = discountRate;
        this.gradeName = gradeName;
    }

    public Integer calculate(Integer price) {
        return (int) (price * discountRate);
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public String getGradeName() {
        return gradeName;
    }

    public static Grade findGradeByGradeValue(Integer gradeValue) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.gradeValue.equals(gradeValue))
                .findFirst()
                .orElseThrow(() -> new InvalidGradeException("Invalid grade value"));
    }

    public static Grade findGradeByGradeName(String gradeName) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.gradeName.equalsIgnoreCase(gradeName))
                .findFirst()
                .orElseThrow(() -> new InvalidGradeException("Invalid grade value"));
    }
}
