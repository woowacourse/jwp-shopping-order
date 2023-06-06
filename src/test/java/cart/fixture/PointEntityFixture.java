package cart.fixture;

import java.sql.Timestamp;

import cart.entity.PointEntity;

public class PointEntityFixture {

    public static final PointEntity ODO1_1 = new PointEntity(
            300, 200, 1L,
            Timestamp.valueOf("2024-01-01 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
    );

    public static final PointEntity ODO1_2 = new PointEntity(
            500, 300, 1L,
            Timestamp.valueOf("2024-02-01 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
    );

    public static final PointEntity ODO1_3 = new PointEntity(
            600, 100, 1L,
            Timestamp.valueOf("2024-03-01 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
    );

    public static final PointEntity ODO1_4 = new PointEntity(
            200, 0, 1L,
            Timestamp.valueOf("2024-04-01 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
    );

    public static final PointEntity ODO2_1 = new PointEntity(
            400, 200, 2L,
            Timestamp.valueOf("2023-12-31 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
    );
}
