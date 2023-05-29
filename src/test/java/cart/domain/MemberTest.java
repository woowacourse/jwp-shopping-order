package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Test
    void 회원이_정상적으로_생성된다() {
        Member member = new Member(1L, "aaa@google.com", "asdf1234", 1000);

        assertAll(
                () -> assertThat(member.getId()).isEqualTo(1L),
                () -> assertThat(member.getEmail()).isEqualTo("aaa@google.com"),
                () -> assertThat(member.getPassword()).isEqualTo("asdf1234"),
                () -> assertThat(member.getPoint().getAmount()).isEqualTo(1000)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {1000, 500})
    void 회원의_포인트가_일정_포인트_이상인_경우_true를_반환한다(long amount) {
        Member member = new Member(1L, "aaa@google.com", "asdf1234", 1000);

        assertThat(member.hasEnoughPoint(new Point(amount))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {1001, 5000})
    void 회원의_포인트가_일정_포인트_미만인_경우_false를_반환한다(long amount) {
        Member member = new Member(1L, "aaa@google.com", "asdf1234", 1000);

        assertThat(member.hasEnoughPoint(new Point(amount))).isFalse();
    }

    @Test
    void 회원의_포인트를_차감한다() {
        Member member = new Member(1L, "aaa@google.com", "asdf1234", 1000);

        member.decreasePoint(new Point(500));

        assertThat(member.getPoint().getAmount())
                .isEqualTo(500);
    }

    @Test
    void 회원의_포인트를_더한다() {
        Member member = new Member(1L, "aaa@google.com", "asdf1234", 1000);

        member.increasePoint(new Point(500));

        assertThat(member.getPoint().getAmount())
                .isEqualTo(1500);
    }

    @Test
    void 회원의_비밀번호가_일치하면_true를_반환한다() {
        Member member = new Member(1L, "aaa@google.com", "1234", 1000);

        assertThat(member.checkPassword("1234"))
                .isTrue();
    }

    @Test
    void 회원의_비밀번호가_일치하지_않으면_false를_반환한다() {
        Member member = new Member(1L, "aaa@google.com", "1234", 1000);

        assertThat(member.checkPassword("1111"))
                .isFalse();
    }
}
