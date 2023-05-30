package cart.domain;

import cart.domain.member.Member;
import cart.exception.MemberNotValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 이메일_값이_공백이라면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Member("", "pizza"))
                .isInstanceOf(MemberNotValidException.class)
                .hasMessage("이메일은 공백일 수 없습니다.");
    }
}
