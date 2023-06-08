package cart.dao.dto.point;

public class PointDto {

    private final Long id;
    private final long memberId;
    private final int point;

    public PointDto(int point, long memberId) {
        this(null, memberId, point);
    }

    public PointDto(Long id, long memberId, int point) {
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
