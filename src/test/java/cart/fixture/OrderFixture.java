package cart.fixture;

import java.sql.Timestamp;
import java.util.List;

import cart.domain.CartItems;
import cart.domain.Order;
import cart.domain.OrderPoint;
import cart.domain.Point;

public class OrderFixture {

    public static Order ORDER1 = Order.of(
            1L,
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

    public static Order ORDER2 = Order.of(
            2L,
            CartItems.of(
                    List.of(
                            CartItemFixture.CHICKEN_QUANTITY,
                            CartItemFixture.PIZZA_QUANTITY,
                            CartItemFixture.SALAD_QUANTITY
                    )
            ),
            new OrderPoint(
                    2L, Point.valueOf(300), Point.valueOf(200)
            ),
            Timestamp.valueOf("2022-05-31 10:00:00")
    );
}
