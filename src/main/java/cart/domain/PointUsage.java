package cart.domain;

public class PointUsage {

    private final Long id;
    private final Long memberId;
    private final Long orderId;
    private final Long pointAdditionId;
    private final Integer amount;

    public PointUsage(Long id, Long memberId, Long orderId, Long pointAdditionId, Integer amount) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.pointAdditionId = pointAdditionId;
        this.amount = amount;
    }

    public static PointUsage from(Long memberId, Long orderId, Long pointAdditionId, Integer amount) {
        return new PointUsage(null, memberId, orderId, pointAdditionId, amount);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getPointAdditionId() {
        return pointAdditionId;
    }

    public Integer getAmount() {
        return amount;
    }
}
