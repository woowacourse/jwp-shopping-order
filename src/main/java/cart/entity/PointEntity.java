package cart.entity;

public class PointEntity {
    private final Long memberId;
    private final int point;

    public PointEntity(Long memberId, int point) {
        this.memberId = memberId;
        this.point = point;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getPoint() {
        return point;
    }
}
