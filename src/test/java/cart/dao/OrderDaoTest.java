package cart.dao;

import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:data.sql", config = @SqlConfig(encoding = "utf-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setup() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void addOrder_ShouldAddOrderAndReturnGeneratedId() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        Coupon coupon = new Coupon(1L, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 1000);
        MemberCoupon memberCoupon = new MemberCoupon(1L, member, coupon, Timestamp.valueOf(LocalDateTime.MAX));
        List<CartItem> cartItems = Collections.singletonList(
            new CartItem(1L, 2,
            new Product(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg"),
            member));
        List<OrderItem> orderItems = cartItems.stream().map(
            cartItem -> new OrderItem(null, null, cartItem.getProduct().getId(), cartItem.getProduct().getName(), cartItem.getProduct().getPrice(), cartItem.getProduct().getImageUrl(), cartItem.getQuantity())
        ).collect(Collectors.toUnmodifiableList());
        Order order = new Order(member, orderItems, memberCoupon);

        Long generatedId = orderDao.save(order);

        assertThat(generatedId).isNotNull();
    }
}
