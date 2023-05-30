package cart.domain;

import java.util.Objects;

public class Price {
    private final int value;

    public Price(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public boolean isOverLimit(final int limit){
        return limit<=this.value;
    }

    public boolean equalValue(final int target){
        return value == target;
    }
}
