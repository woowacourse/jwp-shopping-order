package cart.domain;

import cart.exception.OrderException;
import java.util.Arrays;
import java.util.Objects;

public enum OrderStatus {

    COMPLETE("결제완료"),
    CANCEL("결제취소");

    private final String value;

    OrderStatus(final String value) {
        this.value = value;
    }

    public static OrderStatus find(final String name) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.value, name))
                .findAny()
                .orElseThrow(() -> new OrderException.IllegalOrderStatus(name));
    }

    public String getValue() {
        return value;
    }
}
