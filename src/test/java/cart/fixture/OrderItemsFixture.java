package cart.fixture;

import static cart.fixture.OrderItemFixture.ORDER_ITEM_1;
import static cart.fixture.OrderItemFixture.ORDER_ITEM_2;
import static cart.fixture.OrderItemFixture.ORDER_ITEM_3;

import cart.domain.OrderItems;
import java.util.List;

public class OrderItemsFixture {
    public static final OrderItems ORDER_ITEMS_1 = new OrderItems(List.of(ORDER_ITEM_1, ORDER_ITEM_2, ORDER_ITEM_3));
}
