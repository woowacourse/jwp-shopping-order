package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderRepositoryTest {

    private static final Member MEMBER = new Member(1L, "a@a", "1234");
    private static final int PRICE = 1_000_000_000;
    private static final Product HONG_HONG = new Product(1L, "홍홍", PRICE, "hognhong.com");
    private static final Product HONG_SILE = new Product(2L, "홍실", PRICE, "hongsil.com");
    private static final Optional<Coupon> NOT_NULL_DISCOUNT_RATE_COUPON = Optional.ofNullable(new Coupon("쿠폰", 2d, 0));
    private static final Optional<Coupon> NOT_NULL_DISCOUNT_PRICE_COUPON = Optional.ofNullable(new Coupon("쿠폰", 0d, 5000));
    private static final Optional<Coupon> NULL_COUPON = Optional.empty();

    @Autowired
    private DataSource dataSource;
    private OrderRepository orderRepository;
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private ProductDao productDao;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderRepository = new OrderRepository(
                new OrderDao(dataSource),
                new OrderProductDao(dataSource),
                new CartItemDao(dataSource)
        );
        memberDao = new MemberDao(dataSource);
        productDao = new ProductDao(dataSource);
    }

    @DisplayName("Order 를 저장한다.")
    @ParameterizedTest(name = "쿠폰이 {0} 인 경우")
    @MethodSource("validateCoupon")
    void saveOrder(String testName, Optional<Coupon> coupon, String couponName) {
        initData();
        CartItem hongHongCart = new CartItem(MEMBER, HONG_HONG);
        CartItem hongSileCart = new CartItem(MEMBER, HONG_SILE);
        Order order = Order.of(MEMBER, coupon, List.of(hongHongCart, hongSileCart));
        Order orderAfterSave = orderRepository.save(order);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMember, Order::getCoupon, Order::getCouponName)
                .contains(order.getTimeStamp(), MEMBER, coupon, couponName);
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(hongHongCart.getProduct(), hongSileCart.getProduct());
    }

    private static Stream<Arguments> validateCoupon() {
        return Stream.of(
            Arguments.of("NULL", NULL_COUPON, "적용된 쿠폰이 없습니다."),
            Arguments.of("쿠폰이 Null 이 아닌 경우", NOT_NULL_DISCOUNT_RATE_COUPON, "쿠폰"),
            Arguments.of("쿠폰이 Null 이 아닌 경우", NOT_NULL_DISCOUNT_PRICE_COUPON, "쿠폰")
        );
    }

    private void initData() {
        memberDao.addMember(MEMBER);
        productDao.createProduct(HONG_HONG);
        productDao.createProduct(HONG_SILE);
    }

}