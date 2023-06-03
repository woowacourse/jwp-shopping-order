package cart.fixtures;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixtures.CouponFixtures.COUPON3;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static cart.fixtures.ProductFixtures.*;

public class OrderFixtures {

    public static final OrderItem ORDER_ITEM1 = new OrderItem(1L, PRODUCT2, 3);
    public static final OrderItem ORDER_ITEM2 = new OrderItem(2L, PRODUCT3, 2);
    public static final OrderItem ORDER_ITEM3 = new OrderItem(3L, PRODUCT2, 1);
    public static final OrderItem ORDER_ITEM4 = new OrderItem(4L, PRODUCT3, 1);
    public static final OrderItem ORDER_WITH_COUPON_ITEM_1 = new OrderItem(5L, PRODUCT1, 2);
    public static final OrderItem ORDER_WITH_COUPON_ITEM_2 = new OrderItem(6L, PRODUCT2, 4);
    public static final OrderItem ORDER_WITHOUT_COUPON_ITEM_1 = new OrderItem(5L, PRODUCT1, 2);
    public static final OrderItem ORDER_WITHOUT_COUPON_ITEM_2 = new OrderItem(6L, PRODUCT2, 4);
    public static final OrderItems ORDER_ITEMS_FOR_ORDER1 = new OrderItems(List.of(ORDER_ITEM1, ORDER_ITEM2));
    public static final OrderItems ORDER_ITEMS_FOR_ORDER2 = new OrderItems(List.of(ORDER_ITEM3, ORDER_ITEM4));
    public static final OrderItems ORDER_ITEMS_FOR_ORDER_WITH_COUPON = new OrderItems(List.of(ORDER_WITH_COUPON_ITEM_1, ORDER_WITH_COUPON_ITEM_2));
    public static final OrderItems ORDER_ITEMS_FOR_ORDER_WITHOUT_COUPON = new OrderItems(List.of(ORDER_WITHOUT_COUPON_ITEM_1, ORDER_WITHOUT_COUPON_ITEM_2));
    public static final Order ORDER1 = new Order(1L, ORDER_ITEMS_FOR_ORDER1, MEMBER1, COUPON3, 3000, 86000, LocalDateTime.of(2023, 06, 01, 12, 34, 56));
    public static final Order ORDER2 = new Order(2L, ORDER_ITEMS_FOR_ORDER2, MEMBER1, null, 3000, 36000, LocalDateTime.of(2023, 06, 01, 13, 57, 9));
    public static final Order ORDER_WITH_COUPON = new Order(3L, ORDER_ITEMS_FOR_ORDER_WITH_COUPON, MEMBER1, COUPON3, 3000, 100000, null);
    public static final Order ORDER_WITHOUT_COUPON = new Order(3L, ORDER_ITEMS_FOR_ORDER_WITHOUT_COUPON, MEMBER1, null, 3000, 103000, null);

    public static final OrderRequest ORDER_REQUEST_WITH_COUPON = new OrderRequest(List.of(1L, 2L), 100000, 3L);
    public static final OrderRequest ORDER_REQUEST_WITHOUT_COUPON = new OrderRequest(List.of(1L, 2L), 103000, null);
    public static final OrderResponse ORDER_RESPONSE1 = OrderResponse.of(ORDER1);
    public static final OrderResponse ORDER_RESPONSE2 = OrderResponse.of(ORDER2);
    public static final OrderResponse ORDER_RESPONSE_AFTER_ORDER_WITH_COUPON = OrderResponse.of(ORDER_WITH_COUPON);
    public static final OrderResponse ORDER_RESPONSE_AFTER_ORDER_WITHOUT_COUPON = OrderResponse.of(ORDER_WITHOUT_COUPON);

    public static final OrderDetailResponse ORDER_DETAIL_RESPONSE = OrderDetailResponse.of(ORDER1);
}
