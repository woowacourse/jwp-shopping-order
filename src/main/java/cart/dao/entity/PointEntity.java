package cart.dao.entity;

public class PointEntity {

    private final Long memberId;
    private final Long point;

    public PointEntity(Long memberId, Long point) {
        this.memberId = memberId;
        this.point = point;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPoint() {
        return point;
    }
}
