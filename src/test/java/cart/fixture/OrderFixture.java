package cart.fixture;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.OrderItemsFixture.ORDER_ITEMS_1;
import static cart.fixture.PaymentFixture.PAYMENT_1;

import cart.domain.Order;

public class OrderFixture {
    public static final Order ORDER_1 = new Order(PAYMENT_1, ORDER_ITEMS_1, MEMBER_1);
}
