package cart.persistence.order;

import cart.domain.order.OrderItem;
import cart.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@JdbcTest
class OrderedItemJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderedItemJdbcRepository orderedItemJdbcRepository;

    @BeforeEach
    void setUp() {
        orderedItemJdbcRepository = new OrderedItemJdbcRepository(jdbcTemplate);
    }

    @DisplayName("주문 상품 목록을 저장한다.")
    @Test
    void createOrderItemsTest() {
        List<OrderItem> orderItems = List.of(
                OrderItem.of(1L, 2, ProductFixture.통구이_ID포함),
                OrderItem.of(1L, 3, ProductFixture.배변패드_ID포함),
                OrderItem.of(1L, 1, ProductFixture.꼬리요리_ID포함)
        );

        orderedItemJdbcRepository.createOrderItems(orderItems);
        Assertions.assertThat(orderedItemJdbcRepository.findOrderItemsByOrderId(1L)).hasSize(3);

    }
}
