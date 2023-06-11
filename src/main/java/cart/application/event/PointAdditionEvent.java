package cart.application.event;

import java.time.LocalDateTime;

public class PointAdditionEvent {

    private final long orderId;
    private final long memberId;
    private final int usePointAmount;
    private final int payAmount;
    private final LocalDateTime now;

    public PointAdditionEvent(long orderId, long memberId, int usePointAmount, int payAmount, LocalDateTime now) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.usePointAmount = usePointAmount;
        this.payAmount = payAmount;
        this.now = now;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getUsePointAmount() {
        return usePointAmount;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public LocalDateTime getNow() {
        return now;
    }
}
