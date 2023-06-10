package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("비밀번호가 같은지 확인할 수 있다.")
    void testCheckPassword() {
        // given
        final String password = "passwd";
        final Member member = new Member(1L, "mango@wooteco.com", password);

        // when
        final boolean expected = member.checkPassword(password);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 다르면 false를 반환한다.")
    void testCheckPasswordWhenDifferent() {
        // given
        final String password = "passwd";
        final String differentPassword = "different";
        final Member member = new Member(1L, "mango@wooteco.com", password);

        // when
        final boolean expected = member.checkPassword(differentPassword);

        // then
        assertThat(expected).isFalse();
    }
}
