package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.dto.OrderDto;
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
    private MemberDao memberDao;
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

        this.member = findMemberById(1L);
        this.product1 = createProduct("치킨", 10_000, "http://example.com/chicken.jpg");

    }

    @Test
    void findAll() {
        //given
        final Order order1 = createOrder(member, List.of(OrderItem.notPersisted(product1, 5)));
        final Order order2 = createOrder(member, List.of(OrderItem.notPersisted(product1, 4)));

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
        return memberDao.getMemberById(1L);
    }

    private Product createProduct(final String name, final int price, final String imageUrl) {
        final Product product = new Product(name, price, imageUrl);
        final Long productId = productDao.createProduct(product);
        return new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    private Order createOrder(final Member member, final List<OrderItem> orderItems) {
        final Order order = orderDao.insert(Order.beforePersisted(member, new OrderItems(orderItems)));
        order.getOrderItems().forEach((orderItem -> orderItemDao.insert(order.getId(), orderItem)));
        return order;
    }
}
