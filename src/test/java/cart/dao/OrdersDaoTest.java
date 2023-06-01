package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.Optional;

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

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void findById() {
        final Long id = ordersDao.insert(OrdersEntityFixture.ODO1).getId();
        final Optional<OrdersEntity> result = ordersDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(id),
                () -> assertThat(result.get().getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get().getPointId()).isEqualTo(1L),
                () -> assertThat(result.get().getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.get().getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.get().getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
