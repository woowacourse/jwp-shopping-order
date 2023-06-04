package cart.repository;

import static fixture.CartItemFixture.CART_ITEM_1;
import static fixture.CartItemFixture.CART_ITEM_2;
import static fixture.CartItemFixture.CART_ITEM_3;
import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static fixture.CouponFixture.COUPON_3_NULL;
import static fixture.OrderFixture.ORDER_1;
import static fixture.OrderFixture.ORDER_2;
import static fixture.ProductFixture.PRODUCT_1;
import static fixture.ProductFixture.PRODUCT_2;
import static java.lang.System.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import anotation.RepositoryTest;
import cart.dao.CartItemDao;
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
    @Autowired
    private CartItemDao cartItemDao;

    @DisplayName("Order 를 저장한다.")
    @ParameterizedTest(name = "쿠폰이 {0} 인 경우")
    @MethodSource("validateCoupon")
    void saveOrder(String testName, Coupon coupon, String couponName) {
        CartItem hongHongCart = new CartItem(MemberFixture.MEMBER_1, PRODUCT_1);
        CartItem hongSileCart = new CartItem(MemberFixture.MEMBER_1, PRODUCT_2);
        Order order = Order.of(MemberFixture.MEMBER_1, coupon, List.of(hongHongCart, hongSileCart));
        Order orderAfterSave = orderRepository.save(order);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMember, Order::getOptionalCoupon, Order::getCouponName)
                .contains(order.getTimeStamp(), MemberFixture.MEMBER_1, Optional.ofNullable(coupon), couponName);
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(hongHongCart.getProduct(), hongSileCart.getProduct());
    }

    private static Stream<Arguments> validateCoupon() {
        return Stream.of(
            Arguments.of("NULL", COUPON_3_NULL, "적용된 쿠폰이 없습니다."),
            Arguments.of("쿠폰이 Null 이 아닌 경우", COUPON_1_NOT_NULL_PRICE, "정액 할인 쿠폰"),
            Arguments.of("쿠폰이 Null 이 아닌 경우", COUPON_2_NOT_NULL_RATE, "할인율 쿠폰")
        );
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (성공)")
    void delete_success() {
        orderRepository.deleteCartItems(List.of(1L, 2L));

        List<CartItem> cartItemsAfterDelete = cartItemDao.findByMemberId(MemberFixture.MEMBER_1.getId());

        assertThat(cartItemsAfterDelete).isEmpty();
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (실패)")
    void delete_fail() {
        List<Long> removeCartItemIds = List.of(100L, 101L, 102L);

        assertThatThrownBy(() -> orderRepository.deleteCartItems(removeCartItemIds))
                .isInstanceOf(IllegalArgumentException.class);
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
        List<Order> ordersByMember = orderRepository.findOrdersByMember(MemberFixture.MEMBER_1);

        assertThat(ordersByMember)
                .usingRecursiveComparison()
                .isEqualTo(List.of(ORDER_1, ORDER_2));
    }

    @Test
    @DisplayName("id 들을 가지고 CartItem 들을 조회한다.")
    void findCartItemByIds() {
        List<CartItem> cartItemByIds = orderRepository.findCartItemsByIds(List.of(1L, 2L, 3L));

        assertThat(cartItemByIds)
                .usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(List.of(CART_ITEM_1, CART_ITEM_2, CART_ITEM_3));
    }

}