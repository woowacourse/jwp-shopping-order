package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.ProductOrder;
import cart.factory.CouponFactory;
import cart.factory.ProductFactory;
import java.util.List;
import java.util.Optional;
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

  private OrderDao orderDao;
  private ProductOrderDao productOrderDao;

  @BeforeEach
  void setUp() {
    productOrderDao = new ProductOrderDao(jdbcTemplate);
    orderDao = new OrderDao(jdbcTemplate, productOrderDao);
  }

  @Test
  @DisplayName("주문을 생성하고 찾을 수 있다.")
  void createOrderAndFind() {
    //given
    final ProductOrder chicken = new ProductOrder(ProductFactory.createProduct(1L, "치킨", 10000, "chicken"), 2);
    final ProductOrder salad = new ProductOrder(ProductFactory.createProduct(2L, "샐러드", 20000, "salad"), 1);
    final List<ProductOrder> productOrders = List.of(chicken, salad);
    final Coupon coupon = CouponFactory.createCoupon(1L, "1000원 할인", 1000, 10000);
    final Member member = new Member(1L, "email", "password");
    final String address = "address";
    final int deliveryAmount = 3000;

    //when
    final Long orderId = orderDao.createOrder(new Order(productOrders, coupon, new Amount(deliveryAmount), address, member));
    productOrderDao.createProductOrder(chicken, orderId);
    productOrderDao.createProductOrder(salad, orderId);

    //then
    final Order order = orderDao.findOrderByOrderId(orderId).get();
    assertThat(order.getProducts().size()).isEqualTo(2);
    assertThat(order.getAddress()).isEqualTo(address);
    assertThat(order.getDeliveryAmount()).isEqualTo(new Amount(deliveryAmount));
  }

  @Test
  @DisplayName("여러개 주문을 찾을 수 있다.")
  void findAllOrdersByMemberId() {
    //given
    final ProductOrder chicken = new ProductOrder(ProductFactory.createProduct(1L, "치킨", 10000, "chicken"), 2);
    final ProductOrder salad = new ProductOrder(ProductFactory.createProduct(2L, "샐러드", 20000, "salad"), 1);
    final Coupon coupon = CouponFactory.createCoupon(1L, "1000원 할인", 1000, 10000);
    final Member member = new Member(1L, "email", "password");

    //when
    final Long orderId1 = orderDao.createOrder(
        new Order(List.of(chicken), coupon, new Amount(3000), "address", member));
    productOrderDao.createProductOrder(chicken, orderId1);

    final Long orderId2 = orderDao.createOrder(
        new Order(List.of(chicken, salad), coupon, new Amount(3000), "address", member));
    productOrderDao.createProductOrder(chicken, orderId2);
    productOrderDao.createProductOrder(salad, orderId2);

    //then
    final List<Order> orders = orderDao.findAllOrdersByMemberId(member.getId());
    assertThat(orders.get(0).getProducts().size()).isEqualTo(1);
    assertThat(orders.get(1).getProducts().size()).isEqualTo(2);
  }

}
