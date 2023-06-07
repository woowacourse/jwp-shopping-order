package cart.domain.vo;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MoneyTest {

    @ParameterizedTest
    @ValueSource(strings = {"1000", "999", "998", "0"})
    void 현재_금액이_비교_금액과_같거나_큰지_확인한다(String otherValue) {
        // given
        Money 현재_금액 = Money.from("1000");
        Money 비교_금액 = Money.from(otherValue);

        // when
        boolean 현재_금액이_비교_금액과_같거나_큰지_확인한다 = 현재_금액.isEqualOrGreaterThan(비교_금액);

        // then
        assertThat(현재_금액이_비교_금액과_같거나_큰지_확인한다).isTrue();
    }

    @Test
    void 금액을_수량만큼_곱한다() {
        // given
        Money 현재_금액 = Money.from("1000");

        // when
        Money 총금액 = 현재_금액.times(Quantity.from(10));

        // then
        assertThat(총금액).isEqualTo(Money.from("10000"));
    }

    @Test
    void 금액을_다른_값만큼_곱한다() {
        // given
        Money 현재_금액 = Money.from("1000");

        // when
        Money 총금액 = 현재_금액.times(BigDecimal.TEN);

        // then
        assertThat(총금액).isEqualTo(Money.from("10000"));
    }

    @Test
    void 금액을_다른_금액과_더한다() {
        // given
        Money 현재_금액 = Money.from("1000");

        // when
        Money 더한_금액 = 현재_금액.plus(Money.from("1000"));

        // then
        assertThat(더한_금액).isEqualTo(Money.from("2000"));
    }

    @Test
    void 금액을_다른_금액과_뺀다() {
        // given
        Money 현재_금액 = Money.from("1000");

        // when
        Money 더한_금액 = 현재_금액.minus(Money.from("1000"));

        // then
        assertThat(더한_금액).isEqualTo(Money.ZERO);
    }
}
