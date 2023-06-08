package cart.repository;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static fixture.CouponFixture.빈_쿠폰;
import static fixture.OrderFixture.주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개;
import static fixture.OrderFixture.주문_유저_1_할인율_쿠폰_치킨_2개;
import static fixture.ProductFixture.상품_치킨;
import static fixture.ProductFixture.상품_샐러드;
import static org.assertj.core.api.Assertions.assertThat;

import anotation.RepositoryTest;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Order;
import cart.domain.OrderProduct;
import fixture.MemberFixture;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("Order 를 저장한다.")
    @ParameterizedTest(name = "쿠폰이 {0} 인 경우")
    @MethodSource("validateCoupon")
    void saveOrder(String testName, Coupon coupon, String couponName) {
        CartItem hongHongCart = new CartItem(MemberFixture.유저_1, 상품_치킨);
        CartItem hongSileCart = new CartItem(MemberFixture.유저_1, 상품_샐러드);
        Order order = Order.of(MemberFixture.유저_1, coupon, List.of(hongHongCart, hongSileCart));
        Order orderAfterSave = orderRepository.save(order);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMember, Order::getOptionalCoupon, Order::getCouponName)
                .contains(order.getTimeStamp(), MemberFixture.유저_1, Optional.ofNullable(coupon), couponName);
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(hongHongCart.getProduct(), hongSileCart.getProduct());
    }

    private static Stream<Arguments> validateCoupon() {
        return Stream.of(
            Arguments.of("NULL", 빈_쿠폰, "적용된 쿠폰이 없습니다."),
            Arguments.of("쿠폰이 Null 이 아닌 경우", 정액_할인_쿠폰, "정액 할인 쿠폰"),
            Arguments.of("쿠폰이 Null 이 아닌 경우", 할인율_쿠폰, "할인율 쿠폰")
        );
    }

    /*
    INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
    INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
    INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

    INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, CURRENT_TIMESTAMP, 1);
    INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, CURRENT_TIMESTAMP, 2);

    INSERT INTO order_product(order_id, product_id, quantity) VALUES (1, 1, 2);
    INSERT INTO order_product(order_id, product_id, quantity) VALUES (1, 2, 2);
    INSERT INTO order_product(order_id, product_id, quantity) VALUES (1, 3, 2);
    INSERT INTO orders_product(order_id, product_id, quantity) VALUES (2, 1, 2);
     */
    @Test
    @DisplayName("주문 내역을 조회한다.")
    void findOrderByMember() {
        List<Order> ordersByMember = orderRepository.findOrdersByMember(MemberFixture.유저_1);

        assertThat(ordersByMember)
                .usingRecursiveComparison()
                .ignoringFields("OrderProducts.OrderProduct.id")
                .isEqualTo(List.of(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개, 주문_유저_1_할인율_쿠폰_치킨_2개));
    }

}