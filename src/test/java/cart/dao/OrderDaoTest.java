package cart.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

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
import cart.domain.Order;
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
        List<CartItem> cartItems = Collections.singletonList(
            new CartItem(1L, 2,
            new Product(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg"),
            member));
        Order order = new Order(member, cartItems, coupon);

        Long generatedId = orderDao.addOrder(order);

        assertThat(generatedId).isNotNull();
    }
}
