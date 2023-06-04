package cart.application.dto;

public class GetPointResponse {

    private final int currentPoint;
    private final int toBeExpiredPoint;

    public GetPointResponse(int currentPoint, int toBeExpiredPoint) {
        this.currentPoint = currentPoint;
        this.toBeExpiredPoint = toBeExpiredPoint;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public int getToBeExpiredPoint() {
        return toBeExpiredPoint;
    }
}
