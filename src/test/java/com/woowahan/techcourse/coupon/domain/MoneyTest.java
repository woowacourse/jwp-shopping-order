package com.woowahan.techcourse.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.coupon.exception.CouponException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MoneyTest {

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 100, 1000, 10000})
    void 금액이_0원_이상이면_생성에_성공한다(long value) {
        // given
        // when
        Money money = new Money(value);

        // then
        assertSoftly(softly -> {
            softly.assertThat(money).isNotNull();
            softly.assertThat(money.getValue()).isEqualTo(BigDecimal.valueOf(value));
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -100, -1000, -10000})
    void 금액이_0원_미만이면_생성에_실패한다(long value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Money(value))
                .isInstanceOf(CouponException.class)
                .hasMessage("금액은 0원 이상이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 100", "10, 1000", "100, 10000"})
    void 금액의_비율에_해당하는_금액을_반환한다(int input, int expected) {
        // given
        Money money = new Money(10000);

        // when
        Money result = money.getMoneyByPercentage(input);

        // then
        assertThat(result.getValue().doubleValue()).isEqualTo(BigDecimal.valueOf(expected).doubleValue());
    }

    @Test
    void 금액_비율만큼_금액을_감한다() {
        // given
        Money money = new Money(10000);

        // when
        Money result = money.subtractAmountByPercentage(10);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(9000));
    }

    @Test
    void 금액을_더한다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(20000);

        // when
        Money result = money1.add(money2);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(30000));
    }

    @Test
    void 금액을_뺀다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(20000);

        // when
        Money result = money2.subtract(money1);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(10000));
    }

    @ParameterizedTest
    @CsvSource(value = {"10000, 20000, false", "20000, 10000, true"})
    void 금액이_더_큰지_확인한다(int target, int input, boolean expected) {
        // given
        Money money1 = new Money(target);
        Money money2 = new Money(input);

        // when
        boolean result = money1.isBiggerThan(money2);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void ZERO_금액은_0_원이다() {
        // given
        // when
        Money money = Money.ZERO;

        // then
        assertThat(money.getValue()).isZero();
    }

    @Test
    void 같은_금액이면_같은_객체다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(10000);

        // when
        // then
        assertThat(money1).isEqualTo(money2);
    }

    @Test
    void 다른_금액이면_다른_객체다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(20000);

        // when
        // then
        assertThat(money1).isNotEqualTo(money2);
    }
}
