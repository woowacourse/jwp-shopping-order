package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderedItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.entity.OrderEntity;
import cart.fixture.Fixture;

@JdbcTest
class OrderRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    OrderDao orderDao;
    CartItemDao cartItemDao;
    OrderedItemDao orderedItemDao;
    ProductDao productDao;
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        orderedItemDao = new OrderedItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        orderRepository = new OrderRepository(orderDao, cartItemDao, orderedItemDao, productDao);
    }

    @Test
    @DisplayName("orderId를 인자로 전달받아 Order를 반환한다.")
    void findOrderById() {
        //given
        final List<CartItem> items = List.of(Fixture.CART_ITEM1, Fixture.CART_ITEM2);
        final Long orderId = orderDao.addOrder(new OrderEntity(10000, 1L));
        orderedItemDao.saveAll(items, orderId);

        //when
        final Order result = orderRepository.findOrderById(orderId, Fixture.GOLD_MEMBER);

        //then
        Assertions.assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderId),
                () -> assertThat(result.getPrice()).isEqualTo(10000),
                () -> assertThat(result.getProducts().size()).isEqualTo(2),
                () -> assertThat(result.getProducts().get(0).getQuantity()).isEqualTo(Fixture.CART_ITEM1.getQuantity()),
                () -> assertThat(result.getProducts().get(1).getQuantity()).isEqualTo(Fixture.CART_ITEM2.getQuantity())
        );
    }
}
