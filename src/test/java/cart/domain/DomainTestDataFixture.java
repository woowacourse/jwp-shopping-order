package cart.domain;

import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.PRODUCT_1;

import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.CartItem;
import cart.domain.vo.Quantity;
import java.util.List;

public class DomainTestDataFixture {

    //Cart-item
    public static final CartItem CART_ITEM_1 = new CartItem(2, MEMBER_1, PRODUCT_1);
    public static final CartItem CART_ITEM_2 = new CartItem(4, MEMBER_1, PRODUCT_1);

    //OrderProduct
    public static final OrderProduct ORDER_PRODUCT_1 = new OrderProduct(PRODUCT_1, new Quantity(4));

    //Order`
    public static final Order ORDER_NO_USE_COUPON = Order.of(MEMBER_1, List.of(CART_ITEM_1));
}
