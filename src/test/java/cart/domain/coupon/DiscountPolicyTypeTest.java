package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.VO.Money;
import cart.exception.coupon.DiscountPolicyNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DiscountPolicyTypeTest {

    @Test
    void 입력받는_값에_해당하는_정책이_없으면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> DiscountPolicyType.from("INVALID"))
                .isInstanceOf(DiscountPolicyNotFoundException.class)
                .hasMessage("할인 정책을 찾을 수 없습니다.");
    }

    @Test
    void 이름에_해당하는_할인_정책을_반환한다() {
        // expect
        assertThat(DiscountPolicyType.from("PRICE")).isEqualTo(DiscountPolicyType.PRICE);
    }

    @CsvSource({
            "NONE, 0, 30000",
            "PRICE, 3000, 27000",
            "PERCENT, 30, 21000",
            "DELIVERY, 0, 30000",
    })
    @ParameterizedTest(name = "총 가격이 3만원일 때 할인 정책 : {0} 할인 값: {1} 결과 : {2}")
    void 금액을_할인한다(final DiscountPolicyType type, final Long value, final Long result) {
        // given
        final Money price = Money.from(30000L);

        // expect
        assertThat(type.calculateDiscountValue(value, price)).isEqualTo(Money.from(result));
    }

    @CsvSource({
            "NONE, 0, 3000",
            "PRICE, 3000, 3000",
            "PERCENT, 30, 3000",
            "DELIVERY, 3000, 0",
    })
    @ParameterizedTest(name = "배달비가 3천원일 때 할인 정책 : {0} 할인 값: {1} 결과 : {2}")
    void 배달비를_할인한다(final DiscountPolicyType type, final Long value, final Long result) {
        // given
        final Money price = Money.from(3000L);

        // expect
        assertThat(type.calculateDeliveryFee(value, price)).isEqualTo(Money.from(result));
    }

    @Test
    void 할인된_가격이_0원_아래로_내려가는_경우_0원을_반환한다() {
        // given
        final Money price = Money.from(30000L);

        // when
        final Money result = DiscountPolicyType.PERCENT.calculateDiscountValue(101L, price);

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}
