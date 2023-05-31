package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("멤버 도메인 관련 테스트")
class MemberTest {

    @DisplayName("비밀번호를 체크한다.")
    @Test
    void checkPassword() {
        // given
        String email = "email";
        String password = "password";

        Member member = new Member(null, email, password);

        // when
        boolean check = member.checkPassword(password);

        // then
        assertTrue(check);
    }
}
