package cart.domain;

public enum OrderStatus {

    PENDING("결제완료"),
    PROCESSING("배송준비중"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    CANCELLED("주문취소");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean canNotCancel() {
        return this != PENDING;
    }

    public String getDisplayName() {
        return displayName;
    }
}
