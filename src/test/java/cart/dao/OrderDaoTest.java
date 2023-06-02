package cart.dao;

import cart.entity.OrderEntity;
import cart.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("Order Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderDaoTest {

    @Autowired
    private DataSource dataSource;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
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

    @Test
    @DisplayName("주문 조회 성공")
    void findById_success() {
        // given
        final long id = 1L;

        // when
        final OrderEntity orderEntity = orderDao.findById(id);

        // then
        assertAll(
                () -> assertThat(orderEntity.getId()).isEqualTo(1L),
                () -> assertThat(orderEntity.getMemberId()).isEqualTo(1L),
                () -> assertThat(orderEntity.getOriginalPrice()).isEqualTo(23000),
                () -> assertThat(orderEntity.getDiscountPrice()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("주문 조회 실패 - 잘못된 id")
    void findById_fail_invalid_id() {
        // given
        final long id = 111111L;

        // when, then
        assertThatThrownBy(() -> orderDao.findById(id))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
