package cart.dto;

public class PointResponse {

    private Long availablePoint;

    public PointResponse() {
    }

    public PointResponse(final Long availablePoint) {
        this.availablePoint = availablePoint;
    }

    public Long getAvailablePoint() {
        return availablePoint;
    }
}
