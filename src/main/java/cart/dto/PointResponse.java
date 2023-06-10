package cart.dto;

public class PointResponse {

    private final Long point;

    private PointResponse(Long point) {
        this.point = point;
    }

    public static PointResponse from(Long point) {
        return new PointResponse(point);
    }

    public Long getPoint() {
        return point;
    }
}
