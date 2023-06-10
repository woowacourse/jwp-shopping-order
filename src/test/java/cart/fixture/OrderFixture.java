package cart.fixture;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.OrderItemsFixture.ORDER_ITEMS_1;
import static cart.fixture.PaymentFixture.PAYMENT_1;

import cart.domain.Order;
import java.time.LocalDateTime;

public class OrderFixture {
    public static final Order ORDER_1 = new Order(1L, LocalDateTime.parse("2022-06-03T23:00:15.317115"), PAYMENT_1, ORDER_ITEMS_1, MEMBER_1);
    public static final Order ORDER_2 = new Order(2L, LocalDateTime.parse("2023-05-01T23:00:15.317115"), PAYMENT_1, ORDER_ITEMS_1, MEMBER_1);
}
