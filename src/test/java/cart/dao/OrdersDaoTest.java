package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import cart.entity.OrdersEntity;
import cart.fixture.OrdersEntityFixture;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("최근 주문을 먼저 가져온다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void findByMemberId() {
        final Long id1 = ordersDao.insert(OrdersEntityFixture.ODO1).getId();
        final Long id2 = ordersDao.insert(OrdersEntityFixture.ODO2).getId();
        final List<OrdersEntity> result = ordersDao.findByMemberId(1L);
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(id2),
                () -> assertThat(result.get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getPointId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getEarnedPoint()).isEqualTo(300),
                () -> assertThat(result.get(0).getUsedPoint()).isEqualTo(400),
                () -> assertThat(result.get(0).getCreatedAt()).isEqualTo(Timestamp.valueOf("2024-05-31 10:00:00")),
                () -> assertThat(result.get(1).getId()).isEqualTo(id1),
                () -> assertThat(result.get(1).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getPointId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.get(1).getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.get(1).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
