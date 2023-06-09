package cart.dto.point;

public class PointResponse {
    private Long usablePoint;

    public PointResponse() {
    }

    public PointResponse(Long usablePoint) {
        this.usablePoint = usablePoint;
    }

    public Long getUsablePoint() {
        return usablePoint;
    }
}
