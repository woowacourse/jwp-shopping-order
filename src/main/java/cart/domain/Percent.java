package cart.domain;

import java.util.Objects;

public class Percent {
    private final int value;

    public Percent(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("0 ~ 100 퍼센트까지 가능합니다.");
        }
    }

    public boolean isZero() {
        return value == 0;
    }

    public double getPercentage() {
        return value / 100.0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percent that = (Percent) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
