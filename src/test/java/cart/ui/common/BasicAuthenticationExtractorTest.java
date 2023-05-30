package cart.ui.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.exception.AuthenticationException;
import cart.ui.common.BasicAuthenticationExtractor;
import cart.ui.common.MemberAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.Base64Utils;

class BasicAuthenticationExtractorTest {

    private final BasicAuthenticationExtractor extractor = new BasicAuthenticationExtractor();

    @Nested
    @DisplayName("extract 메서드는 ")
    class Extract {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("인증 정보가 존재하지 않으면 예외를 던진다.")
        void extractWithBlack(String authorization) {
            assertThatThrownBy(() -> extractor.extract(authorization))
                    .isInstanceOf(AuthenticationException.class)
                    .hasMessage("인증 정보가 존재하지 않습니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"Basic", "Basic ", "Bearer a"})
        @DisplayName("인증 정보 형식과 일치하지 않으면 예외를 던진다.")
        void extractWitInvalidFormat(String authorization) {
            assertThatThrownBy(() -> extractor.extract(authorization))
                    .isInstanceOf(AuthenticationException.class)
                    .hasMessage("인증 정보 형식과 일치하지 않습니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "abc:def:ghi"})
        @DisplayName("유효한 인증 정보 개수와 일치하지 않으면 예외를 던진다.")
        void extractWithInvalidAuthSize(String authorization) {
            String encodedAuth = Base64Utils.encodeToUrlSafeString(authorization.getBytes());

            assertThatThrownBy(() -> extractor.extract("Basic " + encodedAuth))
                    .isInstanceOf(AuthenticationException.class)
                    .hasMessage("유효한 인증 정보 개수와 일치하지 않습니다.");
        }

        @Test
        @DisplayName("유효한 인증이라면 멤버 인증 정보를 반환한다.")
        void extract() {
            String auth = "a@a.com:password1";
            String encodedAuth = Base64Utils.encodeToUrlSafeString(auth.getBytes());

            MemberAuth result = extractor.extract("Basic " + encodedAuth);

            assertAll(
                    () -> assertThat(result.getEmail()).isEqualTo("a@a.com"),
                    () -> assertThat(result.getPassword()).isEqualTo("password1")
            );
        }
    }
}
