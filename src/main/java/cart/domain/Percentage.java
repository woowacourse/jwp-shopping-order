package cart.domain;

import java.util.Objects;

public class Percentage {
    private final double percentage;

    public Percentage(int value) {
        validate(value);
        this.percentage = value / 100.0;
    }

    private void validate(int value) {
        if (value < 1 || value > 100) {
            throw new IllegalArgumentException("1 ~ 100 퍼센트까지 가능합니다.");
        }
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percentage that = (Percentage) o;
        return Double.compare(that.percentage, percentage) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(percentage);
    }
}
