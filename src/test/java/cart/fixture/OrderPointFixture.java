package cart.fixture;

import cart.domain.OrderPoint;
import cart.domain.Point;

public class OrderPointFixture {

    public static final OrderPoint ORDER_POINT1 = new OrderPoint(1L, Point.valueOf(200), Point.valueOf(100));
    public static final OrderPoint ORDER_POINT2 = new OrderPoint(2L, Point.valueOf(400), Point.valueOf(300));
}
