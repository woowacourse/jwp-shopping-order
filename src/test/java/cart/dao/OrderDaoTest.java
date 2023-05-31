package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.delivery.BasicDeliveryPolicy;
import cart.domain.discount.BasicDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.price.OrderPrice;
import cart.dto.OrderDto;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private CartItemDao cartItemDao;
    private OrderDao orderDao;
    private ProductDao productDao;
    private OrderItemDao orderItemDao;
    private Product product1;
    private Member member;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);

        this.member = findMemberById(1L);
        this.product1 = createProduct("치킨", 10_000, "http://example.com/chicken.jpg");

    }

    @DisplayName("주문을 생성한다.")
    @Test
    void insert() {
        //given
        createCartItem(new CartItem(member, product1));
        final Order order = createOrder(member, List.of(OrderItem.notPersisted(product1, 1)));
        final OrderPrice orderPrice = new OrderPrice(order.getProductPrice(), new BasicDiscountPolicy(),
            new BasicDeliveryPolicy());
        //when
        final Order persistedOrder = orderDao.insert(order, orderPrice);

        //then
        Assertions.assertThat(persistedOrder.getOrderTime()).isEqualTo(order.getOrderTime());
        Assertions.assertThat(persistedOrder.getProductPrice()).isEqualTo(order.getProductPrice());
        Assertions.assertThat(persistedOrder.getMember()).usingRecursiveComparison().isEqualTo(order.getMember());
    }

    @DisplayName("멤버의 주문 내역을 모두 가져온다.")
    @Test
    void findAllByMemberId() {
        //given
        createCartItem(new CartItem(member, product1));
        final Order order1 = createOrder(member, List.of(OrderItem.notPersisted(product1, 1)));
        createCartItem(new CartItem(member, product1));
        final Order order2 = createOrder(member, List.of(OrderItem.notPersisted(product1, 1)));

        //when
        final List<OrderDto> orderDtos = orderDao.findAllByMemberId(member.getId());

        //then
        assertAll(
            () -> assertThat(orderDtos).hasSize(2),
            () -> assertThat(orderDtos.get(0).getOrderId()).isEqualTo(order1.getId()),
            () -> assertThat(orderDtos.get(1).getOrderId()).isEqualTo(order2.getId())
        );
    }

    private Member findMemberById(final Long memberId) {
        return memberDao.getMemberById(memberId);
    }

    private Product createProduct(final String name, final int price, final String imageUrl) {
        final Product product = new Product(name, price, imageUrl);
        return productDao.createProduct(product);
    }

    private Order createOrder(final Member member, final List<OrderItem> orderItems) {
        final Order order = Order.beforePersisted(member, new OrderItems(orderItems), LocalDateTime.now());
        final OrderPrice orderPrice = new OrderPrice(order.getProductPrice(), new BasicDiscountPolicy(),
            new BasicDeliveryPolicy());

        final Order persistedOrder = orderDao.insert(order, orderPrice);
        order.getOrderItems().forEach((orderItem -> orderItemDao.insert(persistedOrder.getId(), orderItem)));
        return persistedOrder;
    }

    private void createCartItem(CartItem cartItem) {
        cartItemDao.save(cartItem);
    }
}
