package cart.domain;

import cart.exception.OrderServerException;

import java.util.Arrays;

public enum OrderStatus {

    PENDING(1, "Pending"),
    PROCESSING(2, "Processing"),
    SHIPPED(3, "Shipped"),
    DELIVERED(4, "Delivered"),
    CANCELLED(5, "Canceled");

    private static final String INVALID_FIND_ORDER_MESSAGE = "주문 번호를 찾을 수 없습니다.";

    private final int orderStatusId;
    private final String orderStatus;

    OrderStatus(int orderStatusId, String orderStatus) {
        this.orderStatusId = orderStatusId;
        this.orderStatus = orderStatus;
    }

    public static OrderStatus findOrderStatusById(int orderStatusId) {
        return Arrays.stream(values())
                .filter(orderStatus -> orderStatus.orderStatusId == orderStatusId)
                .findFirst()
                .orElseThrow(() -> new OrderServerException(INVALID_FIND_ORDER_MESSAGE));
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
