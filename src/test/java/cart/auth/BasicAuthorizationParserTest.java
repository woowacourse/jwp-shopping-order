package cart.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.exception.auth.InvalidBasicCredentialException;
import java.util.Base64;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class BasicAuthorizationParserTest {

    private final BasicAuthorizationParser basicAuthorizationParser = new BasicAuthorizationParser();

    @CsvSource({
            "AnotherValue, pizza@pizza.com:password",
            "Basic, pizza@pizza.com.password"
    })
    @ParameterizedTest(name = "올바른 Basic 인증 유형이 아니라면 InvalidBasicCredential 예외를_던진다. 입력: {0} {1}")
    void 올바른_Basic_인증_유형이_아니라면_InvalidBasicCredential_예외를_던진다(final String startWith, final String credential) {
        // given
        final String encodedCredential = new String(Base64.getEncoder().encode((credential.getBytes())));
        final String header = startWith + " " + encodedCredential;

        // expect
        assertThatThrownBy(() -> basicAuthorizationParser.parse(header))
                .isInstanceOf(InvalidBasicCredentialException.class)
                .hasMessage("올바른 Basic 형식의 인증정보가 아닙니다. 입력값: " + header);
    }

    @Test
    void 헤더를_입력받아_Credential을_반환한다() {
        // given
        final String credential = "pizza@pizza.com:password";
        final String encodedCredential = new String(Base64.getEncoder().encode((credential.getBytes())));
        final String header = "Basic " + encodedCredential;

        // when
        final Credential result = basicAuthorizationParser.parse(header);

        // then
        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo("pizza@pizza.com"),
                () -> assertThat(result.getPassword()).isEqualTo("password")
        );
    }
}
