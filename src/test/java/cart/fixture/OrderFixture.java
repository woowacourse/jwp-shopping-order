package cart.fixture;

import java.sql.Timestamp;
import java.util.List;

import cart.domain.CartItems;
import cart.domain.Order;
import cart.domain.OrderPoint;
import cart.domain.Point;

public class OrderFixture {

    public static Order ORDER = new Order(
            CartItems.of(
                    List.of(
                            CartItemFixture.CHICKEN,
                            CartItemFixture.PIZZA
                    )
            ),
            new OrderPoint(
                    1L, Point.valueOf(100), Point.valueOf(50)
            ),
            Timestamp.valueOf("2023-05-31 10:00:00")
    );
}
