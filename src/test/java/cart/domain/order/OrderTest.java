package cart.domain.order;

import cart.domain.Coupon;
import cart.domain.Product;
import cart.exception.CouponCannotUseException;
import cart.exception.IllegalOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixtures.CouponFixtures.COUPON1;
import static cart.fixtures.ProductFixtures.PRODUCT1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Nested
    @DisplayName("Order 객체가 생성 가능한지 검증한다.")
    class validateTest {

        @Test
        @DisplayName("최종 금액이 음수이면 예외가 발생한다.")
        void validateIsFinalPricePositiveTest() {
            // given
            int wrongFinalPrice = -1;

            // when, then
            assertThatThrownBy(() -> new Order(null, null, null, null, 0, wrongFinalPrice, null))
                    .isInstanceOf(IllegalOrderException.class)
                    .hasMessage("주문 시 최종 금액은 0원 이상이어야 합니다.");
        }

        @Test
        @DisplayName("주문 상품의 가격 합이 쿠폰의 최소 주문 금액보다 작으면 예외가 발생한다.")
        void validateIsCouponUsableTest() {
            // given
            int minimumOrderAmount = 10000;
            int productPrice = 5000;
            Coupon coupon = new Coupon(null, "", 1000, minimumOrderAmount, null);
            Product product = new Product("", productPrice, "");
            OrderItem orderItem = new OrderItem(null, product, 1);
            OrderItems orderItems = new OrderItems(List.of(orderItem));
            int finalPrice = product.getPrice() - coupon.getDiscountValue();

            // when, then
            assertThatThrownBy(() -> new Order(null, orderItems, null, coupon, 0, finalPrice, null))
                    .isInstanceOf(CouponCannotUseException.class)
                    .hasMessage("상품의 금액이 최소 주문 금액보다 작습니다.");
        }

        @Test
        @DisplayName("입력된 finalPrice와 주문 상품 가격 합과 쿠폰 등을 적용한 가격이 다를 경우 예외가 발생한다.")
        void validateIsRightFinalPriceTest() {
            // given
            Coupon coupon = COUPON1;
            Product product = PRODUCT1;
            OrderItem orderItem = new OrderItem(null, product, 1);
            OrderItems orderItems = new OrderItems(List.of(orderItem));
            int finalPrice = product.getPrice() / 2;

            // when, then
            assertThatThrownBy(() -> new Order(null, orderItems, null, coupon, 0, finalPrice, null))
                    .isInstanceOf(IllegalOrderException.class)
                    .hasMessage("정상적인 주문이 아닙니다.");
        }
    }
}