package cart.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.Member;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private CartItemDao cartItemDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void saveAllOrderItems() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems = cartItemDao.findByMemberId(findMember.getId());
        final CartItems cartItems = new CartItems(findCartItems, findMember);

        final OrderItems orderItems = OrderItems.from(cartItems);
        DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(findMember, orderItems, discountPolicy);

        final Long orderId = orderDao.createOrder(OrderEntity.from(order));
        final List<OrderItemEntity> orderItemEntities = OrderItemEntity.from(orderId, orderItems.getOrderItems());

        // when
        assertDoesNotThrow(() -> orderItemDao.saveAll(orderItemEntities));
    }

    @Test
    void getOrderItemsByOrderId() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems = cartItemDao.findByMemberId(findMember.getId());
        final CartItems cartItems = new CartItems(findCartItems, findMember);

        final OrderItems orderItems = OrderItems.from(cartItems);
        DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(findMember, orderItems, discountPolicy);
        final Long orderId = orderDao.createOrder(OrderEntity.from(order));

        final List<OrderItemEntity> orderItemEntities = OrderItemEntity.from(orderId, orderItems.getOrderItems());
        orderItemDao.saveAll(orderItemEntities);

        // when
        List<OrderItemEntity> findOrderItems = orderItemDao.getOrderItemsByOrderId(orderId);

        // then
        assertThat(findOrderItems).hasSize(orderItems.getOrderItems().size());
    }
}
