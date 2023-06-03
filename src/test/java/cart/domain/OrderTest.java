package cart.domain;

import cart.factory.ProductFactory;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void calculateDiscountedAmount() {
    final Products products = new Products(List.of(
        ProductFactory.createProduct("치킨", 10000, "chicken"),
        ProductFactory.createProduct("피자", 15000, "pizza")));
    final Coupon coupon = new Coupon("1000원 할인", new Amount(1000), new Amount(15000));
    final Member member = new Member(1L, "email", "password");
    final Order order = new Order(products, coupon, new Amount(2000), "address", member);
    final Amount expectedAmount = new Amount(39000);

    final Amount actualAmount = order.calculateDiscountedAmount(List.of(1, 2));

    Assertions.assertThat(actualAmount).isEqualTo(expectedAmount);
  }
}
