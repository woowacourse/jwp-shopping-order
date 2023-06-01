package cart.dao;

import static cart.fixture.DomainFixture.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    OrderItemDao orderItemDao;
    Long orderId;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        OrderDao orderDao = new OrderDao(jdbcTemplate);
        OrderEntity orderEntity = new OrderEntity(1L, 1_000, 1_000,
                10_000, 9_000, LocalDateTime.now());
        orderId = orderDao.save(orderEntity);
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("saveAll은 OrderItemEntities를 전달하면 모든 OrderItem을 저장한다.")
    void saveAllSuccessTest() {
        List<OrderItemEntity> orderItemEntities = List.of(createChickenOrderItemEntity(orderId, 5));

        assertDoesNotThrow(() -> orderItemDao.saveAll(orderItemEntities));
    }

    @Nested
    @DisplayName("주문 상품 조회 테스트")
    class SelectOrderItemTest {

        @BeforeEach
        void setUp() {
            List<OrderItemEntity> orderItemEntities = List.of(createChickenOrderItemEntity(orderId, 3));

            orderItemDao.saveAll(orderItemEntities);
        }

        @Test
        @DisplayName("findByOrderId는 orderId를 전달하면 해당 주문에 맞는 OrderItem을 반환한다.")
        void findByOrderIdSuccessTest() {
            List<OrderItemEntity> actual = orderItemDao.findByOrderId(orderId);

            assertThat(actual).hasSize(1);
        }
    }

    private OrderItemEntity createChickenOrderItemEntity(Long orderId, int quantity) {
        return new OrderItemEntity(
                orderId,
                CHICKEN.getId(),
                CHICKEN.getName(),
                CHICKEN.getPrice(),
                CHICKEN.getImageUrl(),
                quantity
        );
    }
}
