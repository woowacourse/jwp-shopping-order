package cart.fixture;

import static cart.fixture.ProductFixture.PRODUCT_1;
import static cart.fixture.ProductFixture.PRODUCT_2;
import static cart.fixture.ProductFixture.PRODUCT_3;

import cart.domain.OrderItem;

public class OrderItemFixture {
    public static final OrderItem ORDER_ITEM_1 = new OrderItem(PRODUCT_1, 5);
    public static final OrderItem ORDER_ITEM_2 = new OrderItem(PRODUCT_2, 2);
    public static final OrderItem ORDER_ITEM_3 = new OrderItem(PRODUCT_3, 3);
}
