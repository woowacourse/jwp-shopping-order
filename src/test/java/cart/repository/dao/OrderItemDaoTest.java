package cart.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.Member;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.entity.CartItemEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
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
    private ProductDao productDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void saveAllOrderItems() {
        // given
        final Member findMember = memberDao.getMemberById(1L);
        final List<CartItem> findCartItems= cartItemDao.findByMemberId(findMember.getId()).stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toUnmodifiableList());

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
        final List<CartItem> findCartItems= cartItemDao.findByMemberId(findMember.getId()).stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toUnmodifiableList());
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

    private CartItem mapToCartItem(CartItemEntity cartItemEntity) {
        final Member member = memberDao.getMemberById(cartItemEntity.getMemberId());
        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());

        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), Product.from(productEntity), member);
    }
}
