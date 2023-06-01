package cart.dao;

import cart.entity.OrderItemEntity;
import cart.entity.OrderItemWithProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("OrderItem Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderItemDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderItemDao = new OrderItemDao(dataSource);
    }

    @Nested
    @DisplayName("다중 저장")
    class InsertAll {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final List<OrderItemEntity> orderItemEntities = List.of(
                    new OrderItemEntity(1L, 1L, 1), new OrderItemEntity(1L, 2L, 2), new OrderItemEntity(1L, 3L, 4)
            );

            // when, then
            assertThatCode(() -> orderItemDao.insertAll(orderItemEntities))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패 - 잘못된 order id")
        void fail_when_invalid_orderId() {
            // given
            final List<OrderItemEntity> orderItemEntities = List.of(
                    new OrderItemEntity(5L, 1L, 1), new OrderItemEntity(5L, 2L, 2), new OrderItemEntity(5L, 3L, 4)
            );

            // when, then
            assertThatThrownBy(() -> orderItemDao.insertAll(orderItemEntities))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("실패 - 잘못된 product id")
        void fail_when_invalid_productId() {
            // given
            final List<OrderItemEntity> orderItemEntities = List.of(
                    new OrderItemEntity(1L, 1L, 1), new OrderItemEntity(1L, 2L, 2), new OrderItemEntity(1L, 123123L, 4)
            );

            // when, then
            assertThatThrownBy(() -> orderItemDao.insertAll(orderItemEntities))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("order id 로 product join 된 order item 목록 조회")
    class FindProductDetailByOrderId {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final long orderId = 1L;

            // when
            final List<OrderItemWithProductEntity> entities = orderItemDao.findProductDetailByOrderId(orderId);

            // then
            assert entities != null;
            assertAll(
                    () -> assertThat(entities).hasSize(2),
                    () -> assertThat(entities.get(0).getOrderId()).isEqualTo(orderId),
                    () -> assertThat(entities.get(0).getProductId()).isEqualTo(1L)
            );
        }
    }
}
