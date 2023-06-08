package cart.domain;

import cart.factory.ProductFactory;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void calculateDiscountedAmount() {
    final Product chicken = ProductFactory.createProduct("치킨", 10000, "chicken");
    final Product pizza = ProductFactory.createProduct("피자", 15000, "pizza");
    final List<ProductOrder> productOrders = List.of(
        new ProductOrder(chicken, 1),
        new ProductOrder(pizza, 2)
    );
    final Coupon coupon = new Coupon("1000원 할인",1000, 15000);
    final Member member = new Member(1L, "email", "password");
    final Order order = new Order(productOrders, coupon, new Amount(2000), "address", member);
    final Amount expectedAmount = new Amount(39000);

    final Amount actualAmount = order.calculateDiscountedAmount();

    Assertions.assertThat(actualAmount).usingRecursiveComparison().isEqualTo(expectedAmount);
  }
}
