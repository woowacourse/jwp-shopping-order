package cart.entity;

public class PointEntity {

    private final Long id;
    private final Long memberId;
    private final Long point;

    public PointEntity(
            final Long id,
            final Long memberId,
            final Long point
    ) {
        this.id = id;
        this.memberId = memberId;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPoint() {
        return point;
    }
}
