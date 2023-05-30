package cart.domain.price.discount.grade;

import java.util.Arrays;

import cart.exception.InvalidGradeException;

public enum Grade {
    GOLD(1, 0.05),
    SILVER(2, 0.03),
    BRONZE(3, 0.01);

    private final Integer gradeValue;
    private final Double discountRate;

    Grade(Integer gradeValue, Double discountRate) {
        this.gradeValue = gradeValue;
        this.discountRate = discountRate;
    }

    public Integer calculate(Integer price) {
        return (int) (price * discountRate);
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public static Grade findGradeByGradeValue(Integer gradeValue) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.gradeValue.equals(gradeValue))
                .findFirst()
                .orElseThrow(() -> new InvalidGradeException("Invalid grade value"));
    }

    public static Grade findGradeByGradeName(String gradeName) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.name().equalsIgnoreCase(gradeName))
                .findFirst()
                .orElseThrow(() -> new InvalidGradeException("Invalid grade value"));
    }
}
