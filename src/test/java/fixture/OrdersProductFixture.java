package fixture;

import static fixture.ProductFixture.PRODUCT_1;
import static fixture.ProductFixture.PRODUCT_2;
import static fixture.ProductFixture.PRODUCT_3;

import cart.domain.OrderProduct;
import cart.domain.Quantity;

public class OrdersProductFixture {

    public static final OrderProduct ORDER_PRODUCT_1 = new OrderProduct(1L, PRODUCT_1, Quantity.from(2));
    public static final OrderProduct ORDER_PRODUCT_2 = new OrderProduct(2L, PRODUCT_2, Quantity.from(2));
    public static final OrderProduct ORDER_PRODUCT_3 = new OrderProduct(3L, PRODUCT_3, Quantity.from(2));
    public static final OrderProduct ORDER_PRODUCT_4 = new OrderProduct(4L, PRODUCT_1, Quantity.from(2));
    public static final OrderProduct ORDER_PRODUCT_5 = new OrderProduct(5L, PRODUCT_1, Quantity.from(2));
    public static final OrderProduct ORDER_PRODUCT_6 = new OrderProduct(6L, PRODUCT_1, Quantity.from(2));

}
