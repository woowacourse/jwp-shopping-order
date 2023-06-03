package cart.common.dto;

public class PointResponse {
    private final int userPoint;
    private final int minUsagePoint;

    public PointResponse(int userPoint, int minUsagePoint) {
        this.userPoint = userPoint;
        this.minUsagePoint = minUsagePoint;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public int getMinUsagePoint() {
        return minUsagePoint;
    }
}
