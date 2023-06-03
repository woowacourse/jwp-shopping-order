package cart.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductsTest {

  @Test
  void sumPrice() {
    final Products products = new Products(List.of(new Product("치킨", 10000, "chicken"),
        new Product("피자", 15000, "pizza")));
    final Amount expect = new Amount(25000);

    final Amount sumPrice = products.sumPrice();

    Assertions.assertThat(sumPrice).isEqualTo(expect);
  }
}
