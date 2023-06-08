package cart.dao;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.OrderPrice;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestFixture {

    private MemberDao memberDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    public TestFixture(final JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    public Order createOrder(final Member member, final Product product) {
        final OrderItem orderItem = OrderItem.notPersisted(product, 10);
        final Order order = Order.notPersisted(member, new OrderItems(List.of(orderItem)),
            OrderPrice.of(1000L, 100L, 100L, 1000L), LocalDateTime.now());
        return orderDao.insert(order, order.getOrderPrice());
    }

    public Order createOrderAndInsertOrderItem(final Member member, final Product product) {
        final OrderItem orderItem = OrderItem.notPersisted(product, 10);
        final Order order = Order.notPersisted(member, new OrderItems(List.of(orderItem)),
            OrderPrice.of(1000L, 100L, 100L, 1000L), LocalDateTime.now());
        final Order persistedOrder = orderDao.insert(order, order.getOrderPrice());
        orderItemDao.insert(persistedOrder.getId(), orderItem);
        return persistedOrder;
    }

    public Member createMember(final String email, final String password) {
        memberDao.addMember(new Member(null, email, password));
        return memberDao.getMemberByEmail(email).get();
    }

    public Product createProduct(final String name, final int price, final String imageUrl) {
        final Product product = new Product(name, price, imageUrl);
        return productDao.createProduct(product);
    }
}
