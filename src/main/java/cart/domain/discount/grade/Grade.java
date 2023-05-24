package cart.domain.discount.grade;

import java.util.Arrays;
import java.util.function.Function;

import cart.exception.InvalidGradeException;

public enum Grade {
    GOLD(1, price -> (int) (price * 0.05)),
    SILVER(2, price -> (int) (price * 0.03)),
    BRONZE(3, price -> (int) (price * 0.01));

    private final Integer gradeValue;

    private final Function<Integer, Integer> priceDiscounter;

    Grade(Integer gradeValue, Function<Integer, Integer> priceDiscounter) {
        this.gradeValue = gradeValue;
        this.priceDiscounter = priceDiscounter;
    }

    public Integer calculate(Integer price) {
        return priceDiscounter.apply(price);
    }

    public static Grade findGradeByGradeValue(Integer gradeValue) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.gradeValue.equals(gradeValue))
                .findFirst()
                .orElseThrow(() -> new InvalidGradeException("Invalid grade value"));
    }
}
