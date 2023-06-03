package cart.dto;

import cart.domain.Point;

public class UserPointResponse {
    private int points;

    UserPointResponse(){}

    private UserPointResponse(int points) {
        this.points = points;
    }

    public static UserPointResponse of(Point userPoint) {
        return new UserPointResponse(userPoint.point());
    }

    public int getPoints() {
        return points;
    }
}
