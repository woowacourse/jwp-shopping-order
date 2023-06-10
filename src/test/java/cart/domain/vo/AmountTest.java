package cart.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountTest {

    @Test
    @DisplayName("금액이 0보다 작으면 예외가 발생한다.")
    void testValidateUnderZero() {
        assertThatThrownBy(() -> Amount.of(-1))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("금액이 0 이상이면 Amount 가 생성된다.")
    void testValidateIsEqualOrGreaterThanZero() {
        assertDoesNotThrow(() -> Amount.of(0));
    }

    @Test
    @DisplayName("현재 해당 금액이 다른 금액보다 작은지 확인할 수 있다.")
    void testIsLessThan() {
        // given
        final Amount amount = Amount.of(2_000);
        final Amount anotherAmount = Amount.of(5_000);

        // when
        final boolean expected = amount.isLessThan(anotherAmount);

        // then
        assertThat(expected).isTrue();
    }
}
