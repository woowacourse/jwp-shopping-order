package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointEarnPolicyTest {

    Money totalPrice = new Money(10_000);

    @Nested
    @DisplayName("DEFAULT 정책 테스트")
    class DefaultEarnPolicyTest {

        PointEarnPolicy earnPolicy = PointEarnPolicy.DEFAULT;

        @Test
        @DisplayName("calculateEarnPoints는 totalPrice를 전달하면 10%의 적립 포인트를 반환한다.")
        void earnPolicySuccessTest() {
            Money expected = new Money(1_000);
            Money actual = earnPolicy.calculateEarnPoints(totalPrice);

            assertThat(actual).isEqualTo(expected);
        }
    }
}
