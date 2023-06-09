package cart.domain.discountpolicy;

import cart.domain.Money;
import cart.exception.IllegalMoneyAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmountDiscountPolicyTest {

    private final AmountDiscountPolicy amountDiscountPolicy = new AmountDiscountPolicy();

    @Test
    @DisplayName("정량 할인을 적용한다.")
    void amount_discount_test() {
        // given
        Money original = new Money(10000);
        double discountAmount = 1000;

        // when
        final Money discountedMoney = amountDiscountPolicy.apply(original, discountAmount);

        // then
        assertThat(discountedMoney).isEqualTo(new Money(9000));
    }

    @Test
    @DisplayName("초기 금액보다 더 큰 양은 할인하지 못한다")
    void discount_with_larger_amount_test() {
        // given
        Money original = new Money(10000);
        double discountAmount = 10001;

        // when
        // then
        assertThatThrownBy(() -> amountDiscountPolicy.apply(original, discountAmount))
                .isInstanceOf(IllegalMoneyAmountException.class);
    }
}
