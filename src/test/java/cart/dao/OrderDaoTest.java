package cart.dao;

import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("Order Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderDao = new OrderDao(dataSource);
    }

    @Test
    @DisplayName("저장 성공")
    void insert_success() {
        // given
        final OrderEntity orderEntity = new OrderEntity(1L, 15_000, 0);

        // when, then
        assertThatCode(() -> orderDao.insert(orderEntity))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("맴버의 주문 목록 조회 성공")
    void findByMemberId_success() {
        // given
        final long memberId = 1L;

        // when
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(memberId);

        // then
        assert orderEntities != null;
        assertAll(
                () -> assertThat(orderEntities).hasSize(3),
                () -> assertThat(orderEntities.get(0).getMemberId()).isEqualTo(1),
                () -> assertThat(orderEntities.get(0).getOriginalPrice()).isEqualTo(23000),
                () -> assertThat(orderEntities.get(0).getDiscountPrice()).isEqualTo(0)
        );
    }
}
