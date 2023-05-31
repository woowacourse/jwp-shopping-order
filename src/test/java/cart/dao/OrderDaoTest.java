package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
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
class OrderDaoTest {

    private final OrderDao orderDao;

    @Autowired
    public OrderDaoTest(final JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("새 주문 정보를 DB에 저장한다")
    @Test
    void save() {
        // given, when
        final Long createdId = orderDao.save(1L, new Money(3000));

        // then
        assertThat(createdId).isNotNull();
    }

    @DisplayName("주문 정보에 대한 주문 아이템 목록을 DB에 저장한다.")
    @Test
    void saveOrderItems() {
        // given
        final Long createdId = orderDao.save(1L, new Money(3000));

        // when
        assertDoesNotThrow(() -> orderDao.saveOrderItems(
                List.of(new OrderItem(createdId, "doy", new Money(1000), "image.png", 1))));
    }

    @DisplayName("사용자 주문 정보 목록을 DB에서 최신순으로 조회한다.")
    @Test
    void findByMemberId() {
        // given
        final Long createdId = orderDao.save(1L, new Money(3000));
        final OrderItem orderItem = new OrderItem(createdId, "doy", new Money(1000), "image.png", 1);
        final OrderItem orderItem2 = new OrderItem(createdId, "junpak", new Money(1200), "image2.png", 3);
        final OrderItem orderItem3 = new OrderItem(createdId, "urr", new Money(1500), "image3.png", 5);
        orderDao.saveOrderItems(List.of(orderItem, orderItem2, orderItem3));

        // when
        final List<Order> found = orderDao.findByMemberId(1L);

        // then
        assertThat(extractOrderItemsWithoutId(found.get(0).getOrderItems()))
                .containsExactly(orderItem, orderItem2, orderItem3);
    }

    @DisplayName("특정 주문 상세 정보(상품 목록 제외)를 DB에서 조회한다.")
    @Test
    void findDetailById() {
        // given
        final Money deliveryFee = new Money(3000);
        final Long createdId = orderDao.save(1L, deliveryFee);

        // when
        final Order order = orderDao.findDetailById(1L, createdId).get();

        // then
        assertThat(order.getId()).isEqualTo(createdId);
        assertThat(order.getDeliveryFee()).isEqualTo(deliveryFee);
    }

    @DisplayName("특정 주문의 상품 목록을 DB에서 조회한다.")
    @Test
    void findOrderItemsById() {
        // given
        final Money deliveryFee = new Money(3000);
        final Long createdId = orderDao.save(1L, deliveryFee);
        final List<OrderItem> orderItemsToSave = List.of(
                new OrderItem(createdId, "doy", new Money(1000), "image.png", 1),
                new OrderItem(createdId, "junpak", new Money(1000), "image2.png", 3),
                new OrderItem(createdId, "urr", new Money(1000), "image3.png", 5)
        );
        orderDao.saveOrderItems(orderItemsToSave);

        // when
        final List<OrderItem> orderItems = orderDao.findOrderItemsById(1L, createdId);

        // then
        assertThat(extractOrderItemsWithoutId(orderItems)).containsExactlyInAnyOrderElementsOf(orderItemsToSave);
    }

    private List<OrderItem> extractOrderItemsWithoutId(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new OrderItem(item.getOrderId(),
                        item.getName(),
                        new Money(item.getPrice()),
                        item.getImageUrl(),
                        item.getQuantity()))
                .collect(Collectors.toList());
    }
}
