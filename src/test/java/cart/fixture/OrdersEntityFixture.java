package cart.fixture;

import java.sql.Timestamp;

import cart.entity.OrdersEntity;

public class OrdersEntityFixture {

    public static final OrdersEntity ODO1 = new OrdersEntity(
            1L, 1L, 100, 200,
            Timestamp.valueOf("2023-05-31 10:00:00")
    );

    public static final OrdersEntity ODO2 = new OrdersEntity(
            1L, 2L, 300, 400,
            Timestamp.valueOf("2024-05-31 10:00:00")
    );
}
