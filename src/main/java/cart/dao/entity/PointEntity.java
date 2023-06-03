package cart.dao.entity;

public class PointEntity {

    private final Long id;
    private final long memberId;
    private final int point;

    public PointEntity(int point, long memberId) {
        this(null, memberId, point);
    }

    public PointEntity(Long id, long memberId, int point) {
        this.id = id;
        this.memberId = memberId;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getPoint() {
        return point;
    }
}
