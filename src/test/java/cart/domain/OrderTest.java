package cart.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void calculateDiscountedAmount() {
    final Products products = new Products(List.of(new Product("치킨", 10000, "chicken"),
        new Product("피자", 15000, "pizza")));
    final Coupon coupon = new Coupon(new Amount(1000), new Amount(15000));
    final Order order = new Order(products, coupon, new Amount(2000), "address");
    final Amount expectedAmount = new Amount(24000);

    final Amount actualAmount = order.calculateDiscountedAmount();

    Assertions.assertThat(actualAmount).isEqualTo(expectedAmount);
  }
}
