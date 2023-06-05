package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointDiscountPolicyTest {

    @Nested
    @DisplayName("DEFAULT 정책 테스트")
    class DefaultPointDiscountPolicyTest {

        PointDiscountPolicy discountPolicy = PointDiscountPolicy.DEFAULT;
        Money totalPrice = new Money(10_000);

        @Test
        @DisplayName("isUnAvailableUsedPoints는 usedPoints가 totalPrice의 10% 이하인 경우 false를 반환한다.")
        void isUnAvailableUsedPointsTestWithTrue() {
            Money usedPoints = new Money(1_000);

            boolean actual = discountPolicy.isUnAvailableUsedPoints(totalPrice, usedPoints);

            assertThat(actual).isFalse();
        }

        @Test
        @DisplayName("isUnAvailableUsedPoints는 usedPoints가 totalPrice의 10% 초과인 경우 true를 반환한다.")
        void isUnAvailableUsedPointsTestWithFalse() {
            Money usedPoints = new Money(1_001);

            boolean actual = discountPolicy.isUnAvailableUsedPoints(totalPrice, usedPoints);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("calculatePointCondition는 totalPrice를 전달하면 사용할 수 있는 최대 포인트를 반환한다.")
        void calculatePayPriceFailTest() {
            Money actual = discountPolicy.calculatePointCondition(totalPrice);
            Money expected = new Money(1_000);

            assertThat(actual).isEqualTo(expected);
        }
    }
}
