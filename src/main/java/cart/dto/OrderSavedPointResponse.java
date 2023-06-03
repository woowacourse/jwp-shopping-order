package cart.dto;

import cart.domain.Point;

public class OrderSavedPointResponse {

    private int pointsSaved;

    public OrderSavedPointResponse() {
    }

    public OrderSavedPointResponse(int pointsSaved) {
        this.pointsSaved = pointsSaved;
    }

    public static OrderSavedPointResponse from(Point point) {
        return new OrderSavedPointResponse(point.getValue());
    }

    public int getPointsSaved() {
        return pointsSaved;
    }
}
