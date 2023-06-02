package cart.domain;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OrderStatus {

    PENDING("결제완료"),
    PROCESSING("배송준비중"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    CANCELLED("주문취소");

    private static final Map<String, OrderStatus> statusByName = Arrays.stream(values())
        .collect(toMap(OrderStatus::getDisplayName, Function.identity()));
    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public static OrderStatus nameToEnum(String name) {
        return statusByName.get(name);
    }

    public boolean canNotCancel() {
        return this != PENDING;
    }

    public String getDisplayName() {
        return displayName;
    }
}
