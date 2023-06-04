package fixture;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_2;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_1;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_2;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_3;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_4;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_5;
import static fixture.OrdersProductFixture.ORDER_PRODUCT_6;

import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderFixture {

    public static final LocalDateTime TIME = LocalDateTime.of(2023, 1, 1, 12, 0, 0, 0);
    public static final Order ORDER_1 = new Order(1L, TIME, MEMBER_1, COUPON_1_NOT_NULL_PRICE, List.of(ORDER_PRODUCT_1, ORDER_PRODUCT_2, ORDER_PRODUCT_3));
    public static final Order ORDER_2 = new Order(2L, TIME, MEMBER_1, COUPON_2_NOT_NULL_RATE, List.of(ORDER_PRODUCT_4));
    public static final Order ORDER_3 = new Order(3L, TIME, MEMBER_2, COUPON_1_NOT_NULL_PRICE, List.of(ORDER_PRODUCT_5));
    public static final Order ORDER_4 = new Order(4L, TIME, MEMBER_2, COUPON_2_NOT_NULL_RATE, List.of(ORDER_PRODUCT_6));

}
