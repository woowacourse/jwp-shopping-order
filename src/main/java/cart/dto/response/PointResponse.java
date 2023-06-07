package cart.dto.response;

import cart.domain.Point;

public class PointResponse {

    private final int userPoint;
    private final int minUsagePoint;

    public PointResponse(int userPoint) {
        this.userPoint = userPoint;
        this.minUsagePoint = Point.MIN_USAGE_VALUE;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public int getMinUsagePoint() {
        return minUsagePoint;
    }
}
