package fixture;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;
import static fixture.OrdersProductFixture.주문_상품_샐러드_2개;
import static fixture.OrdersProductFixture.주문_상품_치킨_2개;
import static fixture.OrdersProductFixture.주문_상품_치킨_2개_2;
import static fixture.OrdersProductFixture.주문_상품_치킨_2개_3;
import static fixture.OrdersProductFixture.주문_상품_치킨_2개_4;
import static fixture.OrdersProductFixture.주문_상품_피자_2개;

import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static final LocalDateTime TIME = LocalDateTime.of(2023, 1, 1, 12, 0, 0, 0);
    public static final Order 주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개 = new Order(
            1L,
            TIME,
            유저_1,
            정액_할인_쿠폰,
            List.of(주문_상품_치킨_2개, 주문_상품_샐러드_2개, 주문_상품_피자_2개)
    );
    public static final Order 주문_유저_1_할인율_쿠폰_치킨_2개 = new Order(
            2L,
            TIME,
            유저_1,
            할인율_쿠폰,
            List.of(주문_상품_치킨_2개_2)
    );
    public static final Order 주문_유저_2_정액_할인_쿠폰_치킨_2개 = new Order(
            3L,
            TIME,
            유저_2,
            정액_할인_쿠폰,
            List.of(주문_상품_치킨_2개_3)
    );
    public static final Order 주문_유저_2_할인율_쿠폰_치킨_2개 = new Order(
            4L,
            TIME,
            유저_2,
            할인율_쿠폰,
            List.of(주문_상품_치킨_2개_4)
    );

}
