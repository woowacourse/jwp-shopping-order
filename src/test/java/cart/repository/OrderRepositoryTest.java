package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderRepositoryTest {

    private static final Member MEMBER = new Member(1L, "a@a", "1234");
    private static final int PRICE = 1_000_000_000;
    private static final Product HONG_HONG = new Product(1L, "홍홍", PRICE, "hognhong.com");
    private static final Product HONG_SILE = new Product(2L, "홍실", PRICE, "hongsil.com");

    @Autowired
    private DataSource dataSource;
    private OrderRepository orderRepository;
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private ProductDao productDao;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderRepository = new OrderRepository(new OrderDao(dataSource), new OrderProductDao(dataSource));
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Order 를 저장한다.")
    void saveOrder() {
        initData();
        CartItem hongHongCart = new CartItem(MEMBER, HONG_HONG);
        CartItem hongSileCart = new CartItem(MEMBER, HONG_SILE);
        Order order = Order.of(MEMBER, List.of(hongHongCart, hongSileCart));

        Order orderAfterSave = orderRepository.save(order);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMember)
                .contains(order.getTimeStamp(), MEMBER);
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(hongHongCart.getProduct(), hongSileCart.getProduct());
    }

    private void initData() {
        memberDao.addMember(MEMBER);
        productDao.createProduct(HONG_HONG);
        productDao.createProduct(HONG_SILE);
    }

}