package cart.fixtures;

import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixtures.CouponFixtures.COUPON3;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static cart.fixtures.ProductFixtures.PRODUCT2;
import static cart.fixtures.ProductFixtures.PRODUCT3;

public class OrderFixtures {

    public static final OrderItem ORDER_ITEM1 = new OrderItem(1L, PRODUCT2, 3);
    public static final OrderItem ORDER_ITEM2 = new OrderItem(2L, PRODUCT3, 2);
    public static final OrderItem ORDER_ITEM3 = new OrderItem(3L, PRODUCT2, 1);
    public static final OrderItem ORDER_ITEM4 = new OrderItem(4L, PRODUCT3, 1);
    public static final Order ORDER1 = new Order(1L, List.of(ORDER_ITEM1, ORDER_ITEM2), MEMBER1, COUPON3, 3000, 55000, LocalDateTime.of(2023, 06, 01, 12, 34, 56));
    public static final Order ORDER2 = new Order(2L, List.of(ORDER_ITEM3, ORDER_ITEM4), MEMBER1, null, 3000, 28000, LocalDateTime.of(2023, 06, 01, 13, 57, 9));

    public static final OrderResponse ORDER_RESPONSE1 = OrderResponse.of(ORDER1);
    public static final OrderResponse ORDER_RESPONSE2 = OrderResponse.of(ORDER2);

    public static final OrderDetailResponse ORDER_DETAIL_RESPONSE = OrderDetailResponse.of(ORDER1);
}
