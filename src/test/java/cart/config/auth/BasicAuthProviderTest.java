package cart.config.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.application.MemberService;
import cart.dto.User;
import cart.exception.AuthenticationException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Base64Utils;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BasicAuthProviderTest {
    private static final String EMAIL = "glenfiddich@naver.com";
    private static final String PASSWORD = "123456";

    @InjectMocks
    BasicAuthProvider basicAuthProvider;

    @Mock
    MemberService memberService;

    @Test
    void 베이직_형식의_인증_정보가_들어오면_인증_객체가_반환된() {
        String authorization = "Basic Z2xlbmZpZGRpY2hAbmF2ZXIuY29tOjEyMzQ1Ng==";
        given(memberService.getAuthenticate(EMAIL, PASSWORD))
                .willReturn(new User(1L, EMAIL));

        User user = basicAuthProvider.resolveUser(authorization);

        assertThat(user.getMemberId())
                .isEqualTo(1L);
        assertThat(user.getEmail())
                .isEqualTo(EMAIL);
    }

    @Test
    void 베이직_형식의_인증_정보가_아니면_예외가_발생한다() {
        String authorization = "Bearer Z2xlbmZpZGRpY2hAbmF2ZXIuY29tOjEyMzQ1Ng==";

        assertThatThrownBy(() -> basicAuthProvider.resolveUser(
                authorization))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("베이직 형식의 인증 정보가 아닙니다.");
    }

    @Test
    void BASE64_형식의_인코딩_문자열이_아니면_예외가_발생한다() {
        String authorization = "Basic 비밀번호";

        assertThatThrownBy(() -> basicAuthProvider.resolveUser(authorization))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("올바른 형식의 BASE64 문자열이 아닙니다.");
    }

    @Test
    void 올바른_형식의_인증_정보가_아니면_예외가_발생한다() {
        String authorization = "Basic glenfiddich";
        assertThatThrownBy(() -> basicAuthProvider.resolveUser(authorization))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("올바른 형식의 인증 정보가 아닙니다.");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void 인증_정보가_비어있으면_예외가_발생한다(String input) {
        assertThatThrownBy(() -> basicAuthProvider.resolveUser(input))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("인증 헤더가 비어있습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"glen@naver.com:1234:glen", "glen@naver.com,1234"})
    void 인증_정보에_콜론이_두_개_이상이거나_없으면_예외가_발생한다(String input) {
        String authorization = "Basic " + Base64Utils.encodeToString(input.getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> basicAuthProvider.resolveUser(authorization))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("올바른 형식의 인증 정보가 아닙니다.");
    }
}
