package cart.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static cart.fixtures.MemberFixtures.MemberA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Nested
    class checkPassword_테스트 {

        @Test
        void 비밀번호가_같으면_True를_반환하는지_확인하다() {
            assertThat(MemberA.ENTITY.checkPassword("1234")).isTrue();
        }

        @Test
        void 비밀번호가_다르면_False를_반환하는지_확인하다() {
            assertThat(MemberA.ENTITY.checkPassword("4321")).isFalse();
        }
    }

    @Test
    void 포인트를_충전하다() {
        final Member member = Member.of(1L, "test@test.com", "1234", 0L);

        SoftAssertions.assertSoftly(softAssertions -> {
            assertDoesNotThrow(() -> member.deposit(5000L));
            softAssertions.assertThat(member.getCash()).isEqualTo(5000L);
        });
    }

    @Nested
    class withdraw_테스트 {
        @Test
        void 포인트를_차감하다() {
            final Member member = Member.of(1L, "test@test.com", "1234", 5000L);

            SoftAssertions.assertSoftly(softAssertions -> {
                assertDoesNotThrow(() -> member.withdraw(5000L));
                softAssertions.assertThat(member.getCash()).isEqualTo(0L);
            });
        }

        @Test
        void 원래_있는_포인트_이상의_포인트를_차감할_경우_예외를_반환하다() {
            final Member member = Member.of(1L, "test@test.com", "1234", 5000L);

            assertThatThrownBy(() -> member.withdraw(10000L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 0원 미만이 될 수 없습니다");
        }
    }
}
