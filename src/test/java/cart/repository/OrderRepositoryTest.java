package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    OrderRepository orderRepository;

    Long productId;

    Member member;

    @BeforeEach
    void setUp() {
        OrderDao orderDao = new OrderDao(jdbcTemplate);
        OrderItemDao orderItemDao = new OrderItemDao(jdbcTemplate);
        MemberDao memberDao = new MemberDao(jdbcTemplate);
        ProductDao productDao = new ProductDao(jdbcTemplate);

        member = memberDao.findById(1L).get();
        productId = productDao.save(new Product(null, "사과", 1000, "http://image.com/image.png"));

        orderRepository = new OrderRepository(orderDao, orderItemDao, memberDao);
    }

    @Test
    void 주문이_정상적으로_저장된다() {
        Order order = new Order(null, member, List.of(
                new OrderItem(null, new Product(productId, "사과", 1000, "http://image.com/image.png"), 10, 10000)
        ), 0, LocalDateTime.now());

        Order saveOrder = orderRepository.save(order);

        assertThat(saveOrder.getId()).isNotNull();
    }

    @Test
    void 주문을_정상적으로_조회한다() {
        Order order = new Order(null, member, List.of(
                new OrderItem(null, new Product(productId, "사과", 1000, "http://image.com/image.png"), 10, 10000)
        ), 0, LocalDateTime.now());
        Order saveOrder = orderRepository.save(order);

        Order findOrder = orderRepository.findById(saveOrder.getId());

        assertThat(findOrder).isEqualTo(saveOrder);
    }
}
