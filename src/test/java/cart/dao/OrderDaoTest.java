package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.entity.CartItemEntity;
import cart.entity.OrderEntity;
import cart.entity.ProductEntity;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.repository.dao.OrderDao;
import cart.repository.dao.ProductDao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private ProductDao productDao;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void createOrder() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems= cartItemDao.findByMemberId(findMember.getId()).stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toUnmodifiableList());
        final CartItems cartItems = new CartItems(findCartItems, findMember);
        final OrderItems orderItems = OrderItems.from(cartItems);
        DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(findMember, orderItems, discountPolicy);

        // when
        final Long saveId = orderDao.createOrder(OrderEntity.from(order));

        // then
        assertThat(saveId).isNotNull();
    }

    @Test
    void findById() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems= cartItemDao.findByMemberId(findMember.getId()).stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toUnmodifiableList());
        final CartItems cartItems = new CartItems(findCartItems, findMember);
        final OrderItems orderItems = OrderItems.from(cartItems);
        DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(findMember, orderItems, discountPolicy);
        final Long saveId = orderDao.createOrder(OrderEntity.from(order));

        // when
        final Optional<OrderEntity> findOrder = orderDao.findById(saveId);

        // then
        assertThat(findOrder).isPresent();
    }

    private CartItem mapToCartItem(CartItemEntity cartItemEntity) {
        final Member member = memberDao.getMemberById(cartItemEntity.getMemberId());
        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());

        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), Product.from(productEntity), member);
    }
}
