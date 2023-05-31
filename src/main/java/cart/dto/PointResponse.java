package cart.dto;

import cart.domain.Point;

public class PointResponse {
    private Long point;

    public PointResponse() {
    }

    private PointResponse(Long point) {
        this.point = point;
    }

    public static PointResponse of(Point point) {
        return new PointResponse(point.getValue());
    }

    public Long getPoint() {
        return point;
    }
}
