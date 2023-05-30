package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @CsvSource(value = {"password, true", "millie, false"})
    @ParameterizedTest(name = "비밀번호가 password일 때 맞는지 확인한다. 입력: {0} 결과: {1}")
    void 비밀번호를_확인한다(String password, boolean expect) {
        // given
        Member member = new Member("email@email.com", "password");

        // when
        boolean result = member.checkPassword(password);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
