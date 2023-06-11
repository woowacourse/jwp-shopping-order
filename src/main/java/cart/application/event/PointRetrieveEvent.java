package cart.application.event;

public class PointRetrieveEvent {

    private final Long orderId;

    public PointRetrieveEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
