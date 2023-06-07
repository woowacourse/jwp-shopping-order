package cart.dto;

public class PointResponse {

    private Integer availablePoint;

    public PointResponse() {
    }

    public PointResponse(final Integer availablePoint) {
        this.availablePoint = availablePoint;
    }

    public Integer getAvailablePoint() {
        return availablePoint;
    }
}
