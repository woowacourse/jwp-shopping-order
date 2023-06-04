package cart.dto.point;

import cart.domain.Point;

public class OrderSavedPointResponse {

    private int points_saved;

    public OrderSavedPointResponse() {
    }

    public OrderSavedPointResponse(int pointsSaved) {
        this.points_saved = pointsSaved;
    }

    public static OrderSavedPointResponse from(Point point) {
        return new OrderSavedPointResponse(point.getValue());
    }

    public int getPoints_saved() {
        return points_saved;
    }
}
