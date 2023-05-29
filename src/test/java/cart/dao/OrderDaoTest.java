package cart.dao;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
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

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);

        this.product1 = new Product("치킨", 10_000, "http://example.com/chicken.jpg");
    }

    @Test
    void findAll() {
        final Long productId = productDao.createProduct(product1);
        final Member member = memberDao.getMemberById(1L);
        final Order persistedOrder = orderDao.insert(Order.beforePersisted(
            member,
            new OrderItems(
                List.of(
                    OrderItem.notPersisted(product1, 5)
                )
            )
        ), 10_000L);
        orderItemDao.insert(persistedOrder.getId(),
            OrderItem.notPersisted(new Product(productId, "치킨", 10_000, "http://example.com/chicken.jpg"), 5));
        orderDao.findAllByMemberId(member.getId());
    }
}
