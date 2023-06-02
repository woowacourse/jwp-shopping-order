package cart.domain;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static fixture.CouponFixture.COUPON_3_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTest {

    @ParameterizedTest(name = "{0}")
    @DisplayName("Order 생성 성공")
    @MethodSource("validateCoupon")
    void create_success(String testName, Optional<Coupon> coupon, Integer cutPrice, String couponName) {
        Member member = new Member(1L, "홍홍", "honghong");
        Product hongProduct = new Product("홍실", 1_000_000_000, "hong.com");
        CartItem cartItem = new CartItem(member, hongProduct);
        Order order = Order.of(member, coupon, List.of(cartItem, cartItem));

        assertThat(order.getTimeStamp()).isNotNull();
        assertThat(order.getMember()).isEqualTo(member);
        assertThat(order.calculateTotalPrice()).isEqualTo(2_000_000_000);
        assertThat(order.calculateCutPrice()).isEqualTo(cutPrice);
        assertThat(order.getCouponName()).isEqualTo(couponName);
        assertThat(order.getOrderProducts()).hasSize(2)
                .extracting(OrderProduct::getProduct)
                .extracting(Product::getName, Product::getPrice, Product::getImageUrl)
                .contains(tuple("홍실", 1_000_000_000, "hong.com"));
    }

    private static Stream<Arguments> validateCoupon() {
        return Stream.of(
                Arguments.of("Coupon 이 없는 경우 할인되지 않는다.", Optional.ofNullable(COUPON_3_NULL), 0, "적용된 쿠폰이 없습니다."),
                Arguments.of("백분율로 할인하는 Coupon 의 경우 기존의 가격에서 discountRate 만큼 할인된다.", Optional.ofNullable(COUPON_2_NOT_NULL_RATE), 200_000_000, "할인율 쿠폰"),
                Arguments.of("고정된 금액으로 할인하는 Coupon 의 경우 기존의 가격에서 discountPrice 만큼 할인된다.", Optional.ofNullable(COUPON_1_NOT_NULL_PRICE), 5000, "정액 할인 쿠폰")
        );
    }

    @Test
    @DisplayName("Order 생성 실패 (장바구니를 담은 유저와 주문한 유저가 다름)")
    void create_fail() {
        Member member = new Member(1L, "홍홍", "honghong");
        Member illegalMember = new Member(2L, "홍실", "honghong");
        Product hongProduct = new Product("홍실", 1_000_000_000, "hong.com");
        CartItem cartItem = new CartItem(member, hongProduct);
        List<CartItem> cartItems = List.of(cartItem);

        assertThatThrownBy(() -> Order.of(illegalMember, Optional.ofNullable(COUPON_3_NULL), cartItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

}