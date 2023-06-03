package cart.dto;

import cart.domain.Point;

public class MemberPointResponse {

    private int points;

    public MemberPointResponse() {
    }

    public MemberPointResponse(int points) {
        this.points = points;
    }

    public static MemberPointResponse from(Point point) {
        return new MemberPointResponse(point.getValue());
    }

    public int getPoints() {
        return points;
    }
}
