package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.entity.OrderEntity;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.repository.dao.OrderDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;
    private MemberDao memberDao;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void createOrder() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems = cartItemDao.findByMemberId(findMember.getId());
        final CartItems cartItems = new CartItems(findCartItems, findMember);
        final OrderItems orderItems = OrderItems.from(cartItems);
        DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(findMember, orderItems, discountPolicy);

        // when
        final Long saveId = orderDao.createOrder(OrderEntity.from(order));

        // then
        assertThat(saveId).isNotNull();
    }
}
