package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;

import cart.entity.OrdersEntity;
import cart.fixture.OrdersEntityFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class OrdersDaoTest {

    private OrdersDao ordersDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        ordersDao = new OrdersDao(jdbcTemplate);
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void insert() {
        final OrdersEntity result = ordersDao.insert(OrdersEntityFixture.ODO1);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getMemberId()).isEqualTo(1L),
                () -> assertThat(result.getPointId()).isEqualTo(1L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
