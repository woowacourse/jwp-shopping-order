package cart.dto;

import cart.domain.Point;

public class OrderPointResponse {
    private int  points_saved;

    OrderPointResponse(){}

    public OrderPointResponse(int pointsSaved) {
        points_saved = pointsSaved;
    }

    public static OrderPointResponse of(Point savedPoint) {
        return new OrderPointResponse(savedPoint.point());
    }

    public int getPoints_saved() {
        return points_saved;
    }
}
