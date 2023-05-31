package cart.dto;

public class PointResponse {

    private final Long totalPoint;

    private PointResponse(final Long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public static PointResponse from(final Long totalPoint) {
        return new PointResponse(totalPoint);
    }

    public Long getTotalPoint() {
        return totalPoint;
    }
}
