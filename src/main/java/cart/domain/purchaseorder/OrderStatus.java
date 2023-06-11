package cart.domain.purchaseorder;

import java.util.HashMap;

public enum OrderStatus {

    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private static final HashMap<String, OrderStatus> stringToStatus = new HashMap<>();
    static {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            stringToStatus.put(orderStatus.value, orderStatus);
        }
    }

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public static OrderStatus convertTo(String value) {
        return stringToStatus.get(value);
    }

    public String getValue() {
        return value;
    }
}
