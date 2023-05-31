package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dao.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderItemDaoTest {

    private final OrderItemDao orderItemDao;

    @Autowired
    public OrderItemDaoTest(final JdbcTemplate jdbcTemplate) {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @DisplayName("주문 정보에 대한 주문 아이템 목록을 DB에 저장한다.")
    @Test
    void saveOrderItems() {
        // given, when, then
        assertDoesNotThrow(() -> orderItemDao.saveAll(
                List.of(new OrderItemEntity(1L, "doy", 1000, "image.png", 1))));
    }

    @DisplayName("특정 주문의 상품 목록을 DB에서 조회한다.")
    @Test
    void findOrderItemsById() {
        // given
        final long orderId = 1L;
        orderItemDao.saveAll(List.of(
                new OrderItemEntity(orderId, "doy", 1000L, "image.png", 1),
                new OrderItemEntity(orderId, "junpak", 1000L, "image2.png", 3),
                new OrderItemEntity(orderId, "urr", 1000L, "image3.png", 5)
        ));

        // when
        final List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);
        final List<OrderItemEntity> extractedWithoutId = orderItems.stream()
                .map(orderItem -> new OrderItemEntity(orderId, orderItem.getName(), orderItem.getPrice(),
                        orderItem.getImageUrl(), orderItem.getQuantity()))
                .collect(Collectors.toList());

        // then
        assertThat(extractedWithoutId).containsExactly(
                new OrderItemEntity(orderId, "doy", 1000L, "image.png", 1),
                new OrderItemEntity(orderId, "junpak", 1000L, "image2.png", 3),
                new OrderItemEntity(orderId, "urr", 1000L, "image3.png", 5)
        );
    }

    @DisplayName("특정 주문의 상품 목록을 DB에서 삭제한다.")
    @Test
    void deleteOrderItemsById() {
        // given
        final Long orderId = 1L;
        final Long orderId2 = 2L;
        orderItemDao.saveAll(List.of(new OrderItemEntity(orderId, "junpak", 1000L, "image2.png", 3)));
        orderItemDao.saveAll(List.of(new OrderItemEntity(orderId2, "urr", 1000L, "image2.png", 3)));

        // when
        orderItemDao.deleteByOrderId(orderId);

        // then
        assertThat(orderItemDao.findByOrderId(orderId)).isEmpty();
        assertThat(orderItemDao.findByOrderId(orderId2)).hasSize(1);
    }
}
