package cart.entity;

public class PointEntity {

    private final Long id;
    private final Long memberId;
    private final int point;

    public PointEntity(
            final Long id,
            final Long memberId,
            final int point
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

    public int getPoint() {
        return point;
    }
}
