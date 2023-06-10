package cart.domain.member;

import cart.exception.AuthenticationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("루카"));
    }

    @Test
    void 비밀번호가_다르면_예외를_발생한다() {
        //given
        //then
        assertThatThrownBy(() -> member.checkPassword(member.getPassword() + "asdf")).isInstanceOf(AuthenticationException.LoginFail.class);
    }

    @Test
    void 비밀번호가_일치하는지_확인한다() {
        //given
        Member member = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("루카"));
        //then
        Assertions.assertDoesNotThrow(() -> member.checkPassword(member.getPassword() ));
    }
}
