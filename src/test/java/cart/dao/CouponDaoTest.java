package cart.dao;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.factory.CouponFactory;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CouponDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private CouponDao couponDao;
  private OrderDao orderDao;

  @BeforeEach
  void setUp() {
    couponDao = new CouponDao(jdbcTemplate);
    orderDao = new OrderDao(jdbcTemplate);
  }

  @Test
  void createAndFind() {
    final Long productId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));

    final Optional<Coupon> coupon = couponDao.findById(productId);

    Assertions.assertThat(coupon).isPresent();
  }

  @Test
  void test() {
    orderDao.createOrder(new Order(1L, new Member(1L, "email", "password"), new Products(List.of(new Product(1L, "name", new Amount(10000), "imageUrl"))),
        Coupon.empty(), new Amount(3000), "address"));

  }
}
