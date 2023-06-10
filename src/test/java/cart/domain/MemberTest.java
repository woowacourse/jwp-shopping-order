package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberTest {

    @DisplayName("사용자가 보유한 포인트를 충전한다")
    @Test
    void chargePoint() {
        // given
        final Member member = new Member(1L, "a@a.com", "12121212", 1000);

        // when
        final Member actual = member.chargePoint(1000);

        // then
        assertThat(actual.getPoints()).isEqualTo(2000);
    }

    @DisplayName("사용자가 보유한 포인트를 소비한다")
    @Test
    void spendPoint() {
        // given
        final Member member = new Member(1L, "a@a.com", "12121212", 1000);

        // when
        final Member actual = member.spendPoint(1000);

        // then
        assertThat(actual.getPoints()).isEqualTo(0);
    }
}
