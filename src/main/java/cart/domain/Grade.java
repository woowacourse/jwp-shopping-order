package cart.domain;

import java.util.Arrays;

public enum Grade {

    GOLD(0.05),
    SILVER(0.03),
    BRONZE(0.01),
    ;

    private final double discountRate;

    Grade(final double discountRate) {
        this.discountRate = discountRate;
    }

    public int calculateGradeDiscountPrice(final int price) {
        return (int) (price * discountRate);
    }

    public static Grade from(final String name) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 등급은 존재하지 않습니다."));
    }

    public String getName() {
        return name();
    }

    public double getDiscountRate() {
        return discountRate;
    }

}
