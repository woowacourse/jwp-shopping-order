package cart.order.repository;

import cart.init.DBInit;
import cart.member.dao.MemberDao;
import cart.order.dao.OrderDao;
import cart.order.dao.OrderInfoDao;
import cart.order.domain.Order;
import cart.order.domain.OrderInfo;
import cart.productpoint.dao.ProductPointDao;
import cart.product.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.member.domain.MemberTest.MEMBER;
import static cart.product.domain.ProductTest.PRODUCT_FIRST;
import static cart.product.domain.ProductTest.PRODUCT_SECOND;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderRepositoryTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        final OrderDao orderDao = new OrderDao(jdbcTemplate);
        final OrderInfoDao orderInfoDao = new OrderInfoDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);
        final ProductPointDao productPointDao = new ProductPointDao(jdbcTemplate);
        orderRepository = new OrderRepository(orderDao, orderInfoDao, memberDao, productDao, productPointDao);
    }

    @Test
    void 주문을_저장한다() {
        // given
        final OrderInfo orderInfo1 = new OrderInfo(PRODUCT_FIRST, "치킨", 10000L, "aa", 2L);
        final OrderInfo orderInfo2 = new OrderInfo(PRODUCT_SECOND, "샐러드", 20000L, "bb", 4L);
        final Order order = new Order(List.of(orderInfo1, orderInfo2), 100000L, 600L, 10000L);

        // when

        final Long orderId = orderRepository.save(MEMBER.getId(), order);

        // then
        assertThat(orderId).isPositive();
    }

    @Test
    void Member로_주문목록을_조회한다() {
        // given
        final OrderInfo orderInfo1 = new OrderInfo(PRODUCT_FIRST, "치킨", 10000L, "aa", 2L);
        final OrderInfo orderInfo2 = new OrderInfo(PRODUCT_SECOND, "샐러드", 20000L, "bb", 4L);
        final Order order = new Order(List.of(orderInfo1, orderInfo2), 100000L, 600L, 10000L);
        orderRepository.save(MEMBER.getId(), order);

        // when
        final List<Order> orders = orderRepository.findByMember(MEMBER);

        // then
        assertThat(orders).hasSize(1);
    }

    @Test
    void Member와_memberId로_주문을_조회한다() {
        // given
        final OrderInfo orderInfo1 = new OrderInfo(PRODUCT_FIRST, "치킨", 10000L, "aa", 2L);
        final OrderInfo orderInfo2 = new OrderInfo(PRODUCT_SECOND, "샐러드", 20000L, "bb", 4L);
        final Order orderToSave = new Order(List.of(orderInfo1, orderInfo2), 100000L, 600L, 10000L);
        final Long orderId = orderRepository.save(MEMBER.getId(), orderToSave);
        
        // when
        final Order order = orderRepository.findByMemberAndId(MEMBER, orderId);

        // then
        assertThat(order.getId()).isPositive();
    }
}
