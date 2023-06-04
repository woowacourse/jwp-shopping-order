package cart.domain;

import java.util.Arrays;

public enum Grade {

    GOLD,
    SILVER,
    BRONZE,
    ;

    public static Grade from(final String name) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 등급은 존재하지 않습니다."));
    }

    public String getName() {
        return name();
    }
}
