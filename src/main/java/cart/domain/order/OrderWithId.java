package cart.domain.order;

public class OrderWithId {

    private final Long orderId;
    private final Order order;

    public OrderWithId(final Long orderId, final Order order) {
        this.orderId = orderId;
        this.order = order;
    }

    public boolean isNotOwner(final String memberName) {
        return !order.isOwner(memberName);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Order getOrder() {
        return order;
    }
}
