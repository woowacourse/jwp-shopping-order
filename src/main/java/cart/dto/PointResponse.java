package cart.dto;

public class PointResponse {

    private int currentPoint;
    private int toBeExpiredPoint;

    public PointResponse(int currentPoint, int toBeExpiredPoint) {
        this.currentPoint = currentPoint;
        this.toBeExpiredPoint = toBeExpiredPoint;
    }
}
