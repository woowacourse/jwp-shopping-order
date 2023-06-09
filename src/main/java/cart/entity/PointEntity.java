package cart.entity;

public class PointEntity {
    private final Long memberId;
    private final Integer pointAmount;

    public PointEntity(final Long memberId, final Integer pointAmount) {
        this.memberId = memberId;
        this.pointAmount = pointAmount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }
}
