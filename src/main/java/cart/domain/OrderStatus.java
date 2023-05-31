package cart.domain;

public enum OrderStatus {

    PENDING(1, "Pending"),
    PROCESSING(2, "Processing"),
    SHIPPED(3, "Shipped"),
    DELIVERED(4, "Delivered"),
    CANCELLED(5, "Canceled");

    private final int orderStatusId;
    private final String orderStatus;

    OrderStatus(int orderStatusId, String orderStatus) {
        this.orderStatusId = orderStatusId;
        this.orderStatus = orderStatus;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
