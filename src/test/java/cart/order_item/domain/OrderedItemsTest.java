package cart.order_item.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.value_object.Money;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderedItemsTest {

  @Test
  @DisplayName("calculateAllItemPrice() : 주문된 물건들의 총합을 구할 수 있다.")
  void test_calculateAllItemPrice() throws Exception {
    //given
    final List<OrderItem> orderItems = List.of(
        new OrderItem(null, null, new Money(5000), null, 4),
        new OrderItem(null, null, new Money(2500), null, 4),
        new OrderItem(null, null, new Money(20000), null, 5)
    );

    final OrderedItems orderedItems = new OrderedItems(orderItems);

    //when & then
    assertEquals(new Money(130000), orderedItems.calculateAllItemPrice());
  }
}
