package cart.domain.member;

import cart.domain.value.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RankTest {

    @Test
    @DisplayName("일반 회원의 할인률은 0% 이다.")
    void calculate_normal_discount() {
        // given
        final Rank rank = Rank.NORMAL;
        final Money price = new Money(10_000);

        // when
        Money result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(new Money(10_000));
    }

    @Test
    @DisplayName("실버 회원의 할인률은 5% 이다.")
    void calculate_silver_discount() {
        // given
        final Rank rank = Rank.SILVER;
        final Money price = new Money(10_000);

        // when
        Money result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(new Money(9_500));
    }

    @Test
    @DisplayName("골드 회원의 할인률은 10% 이다.")
    void calculate_gold_discount() {
        // given
        final Rank rank = Rank.GOLD;
        final Money price = new Money(10_000);

        // when
        Money result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(new Money(9_000));
    }

    @Test
    @DisplayName("플레티넘 회원의 할인률은 15% 이다.")
    void calculate_platinum_discount() {
        // given
        final Rank rank = Rank.PLATINUM;
        final Money price = new Money(10_000);

        // when
        Money result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(new Money(8_500));
    }

    @Test
    @DisplayName("다이아몬드 회원의 할인률은 20% 이다.")
    void calculate_diamond_discount() {
        // given
        final Rank rank = Rank.DIAMOND;
        final Money price = new Money(10_000);

        // when
        Money result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(new Money(8_000));
    }

    @ParameterizedTest(name = "{displayName}")
    @MethodSource("provideCumulativeAmountAndRank")
    @DisplayName("누적액에 알맞은 등급을 가져온다. 누적금액: {0}, 등급: {1}")
    void find_rank(Money cumulativeAmount, Rank expect) {
        // when
        Rank result = Rank.findRank(cumulativeAmount);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> provideCumulativeAmountAndRank() {
        return Stream.of(
                Arguments.of(new Money(100), Rank.NORMAL),
                Arguments.of(new Money(100_100), Rank.SILVER),
                Arguments.of(new Money(200_100), Rank.GOLD),
                Arguments.of(new Money(300_100), Rank.PLATINUM),
                Arguments.of(new Money(500_100), Rank.DIAMOND)
        );
    }
}
