package cart.common.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import cart.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class BasicTokenProviderTest {

    @Test
    @DisplayName("정상적인 Authorization 헤더 정보면, 디코딩된 토큰 정보를 반환한다.")
    void extractToken_success() {
        // given
        final String authorization = "Basic am91cm5leTp0ZXN0MTIzNA==";

        // when
        final String token = BasicTokenProvider.extractToken(authorization);

        // then
        assertThat(token)
            .isEqualTo("journey:test1234");
    }

    @NullSource
    @ParameterizedTest(name = "Authorization 헤더 정보가 null이면, 예외가 발생한다.")
    void extractToken_null_fail(final String authorization) {
        assertThatThrownBy(() -> BasicTokenProvider.extractToken(authorization))
            .isInstanceOf(AuthenticationException.class);
    }

    @ValueSource(strings = {"", "Basic"})
    @ParameterizedTest(name = "Authorization 헤더 정보가 Basic prefix를 포함할 만큼 길이가 길지 않으면, 예외가 발생한다.")
    void extractToken_length_fail(final String authorization) {
        assertThatThrownBy(() -> BasicTokenProvider.extractToken(authorization))
            .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자의 이름과 비밀번호로 Basic 토큰을 만들어낸다.")
    void createToken() {
        // given
        final String name = "jounrey";
        final String password = "password";

        // when
        final String token = BasicTokenProvider.createToken(name, password);

        // then
        assertThat(token)
            .isEqualTo("am91bnJleTpwYXNzd29yZA==");
    }
}
