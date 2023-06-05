package cart.authentication;

import static cart.fixture.DomainFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.exception.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicAuthorizationExtractorTest {

    BasicAuthorizationExtractor extractor = new BasicAuthorizationExtractor();

    @Test
    @DisplayName("올바른 Basic Token이 전달되면 AuthInfo로 변환한다.")
    void extractSuccess() {
        String encoded = encode(MEMBER_A.getEmail() + ":" + MEMBER_A.getPassword());

        AuthInfo authInfo = extractor.extract("Basic " + encoded);

        assertAll(
                () -> assertThat(authInfo.getEmail()).isEqualTo(MEMBER_A.getEmail()),
                () -> assertThat(authInfo.getPassword()).isEqualTo(MEMBER_A.getPassword())
        );
    }

    @Test
    @DisplayName("Authorization 헤더가 존재하지 않으면 예외가 발생한다.")
    void extractFailWithoutAuthorizationHeader() {
        assertThatThrownBy(() -> extractor.extract(null))
                .isInstanceOf(AuthenticationException.ForbiddenMember.class)
                .hasMessage("로그인이 필요한 기능입니다.");
    }

    @Test
    @DisplayName("Basic Token의 구분자가 존재하지 않으면 예외가 발생한다.")
    void extractFailWithoutBasicTokenDelimiter() {
        String encoded = encode(MEMBER_A.getEmail() + MEMBER_A.getPassword());

        assertThatThrownBy(() -> extractor.extract("Basic " + encoded))
                .isInstanceOf(AuthenticationException.InvalidTokenFormat.class)
                .hasMessage("유효한 토큰 형식이 아닙니다.");
    }

    @Test
    @DisplayName("Basic Token의 타입 Basic 이 누락되면 예외가 발생한다.")
    void extractFailWithoutBasicType() {
        String encoded = encode(MEMBER_A.getEmail() + ":" + MEMBER_A.getPassword());

        assertThatThrownBy(() -> extractor.extract(encoded))
                .isInstanceOf(AuthenticationException.InvalidAuthentication.class)
                .hasMessage("유효한 인증 방식이 아닙니다.");
    }

    private String encode(String userInfo) {
        return new String(Base64.getEncoder()
                .encode(userInfo.getBytes(StandardCharsets.UTF_8)));
    }
}
