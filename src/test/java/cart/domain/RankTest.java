package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RankTest {

    @Test
    @DisplayName("일반 회원의 할인률은 0% 이다.")
    void calculate_normal_discount() {
        // given
        final Rank rank = Rank.NORMAL;
        final int price = 10_000;

        // when
        int result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(10_000);
    }

    @Test
    @DisplayName("실버 회원의 할인률은 5% 이다.")
    void calculate_silver_discount() {
        // given
        final Rank rank = Rank.SILVER;
        final int price = 10_000;

        // when
        int result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(9_500);
    }

    @Test
    @DisplayName("골드 회원의 할인률은 10% 이다.")
    void calculate_gold_discount() {
        // given
        final Rank rank = Rank.GOLD;
        final int price = 10_000;

        // when
        int result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(9_000);
    }

    @Test
    @DisplayName("플레티넘 회원의 할인률은 15% 이다.")
    void calculate_platinum_discount() {
        // given
        final Rank rank = Rank.PLATINUM;
        final int price = 10_000;

        // when
        int result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(8_500);
    }

    @Test
    @DisplayName("다이아몬드 회원의 할인률은 20% 이다.")
    void calculate_diamond_discount() {
        // given
        final Rank rank = Rank.DIAMOND;
        final int price = 10_000;

        // when
        int result = rank.getDiscountPrice(price);

        // then
        assertThat(result).isEqualTo(8_000);
    }
}
