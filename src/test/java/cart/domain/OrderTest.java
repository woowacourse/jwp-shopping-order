package cart.domain;

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

    private static final Optional<Coupon> NOT_NULL_DISCOUNT_RATE_COUPON = Optional.ofNullable(new Coupon("쿠폰", 10d, 0));
    private static final Optional<Coupon> NOT_NULL_DISCOUNT_PRICE_COUPON = Optional.ofNullable(new Coupon("쿠폰", 0d, 5_000_000));
    private static final Optional<Coupon> NULL_COUPON = Optional.empty();

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
                Arguments.of("Coupon 이 없는 경우 할인되지 않는다.", NULL_COUPON, 0, "적용된 쿠폰이 없습니다."),
                Arguments.of("백분율로 할인하는 Coupon 의 경우 기존의 가격에서 discountRate 만큼 할인된다.", NOT_NULL_DISCOUNT_RATE_COUPON, 200_000_000, "쿠폰"),
                Arguments.of("고정된 금액으로 할인하는 Coupon 의 경우 기존의 가격에서 discountPrice 만큼 할인된다.", NOT_NULL_DISCOUNT_PRICE_COUPON, 5_000_000, "쿠폰")
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

        assertThatThrownBy(() -> Order.of(illegalMember, NULL_COUPON, cartItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

}