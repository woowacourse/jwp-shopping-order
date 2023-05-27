package cart.domain;

import java.util.Objects;

public class Percent {
    private final int percent;

    public Percent(int percent) {
        validate(percent);
        this.percent = percent;
    }

    private void validate(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("0 ~ 100 퍼센트까지 가능합니다.");
        }
    }

    public double getPercentage() {
        return percent / 100.0;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percent that = (Percent) o;
        return percent == that.percent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(percent);
    }
}
