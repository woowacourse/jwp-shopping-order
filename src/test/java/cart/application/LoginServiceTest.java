package cart.application;

import cart.exception.AuthenticationException;
import cart.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static cart.fixture.MemberFixture.라잇;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void 로그인() {
        when(memberRepository.findByEmail(라잇.getEmail())).thenReturn(라잇);
        String idAndPassword = 라잇.getEmail() + ":" + 라잇.getPassword();
        String encode = new String(Base64.encodeBase64(idAndPassword.getBytes(StandardCharsets.UTF_8)));

        assertDoesNotThrow(() -> loginService.login("Basic "+ encode));
    }

    @Test
    void 로그인_실패() {
        when(memberRepository.findByEmail(라잇.getEmail())).thenReturn(라잇);
        String idAndPassword = 라잇.getEmail() + ":" + 라잇.getPassword()+"1234";
        String encode = new String(Base64.encodeBase64(idAndPassword.getBytes(StandardCharsets.UTF_8)));

        assertThatThrownBy(() -> loginService.login("Basic "+ encode))
                .isInstanceOf(AuthenticationException.LoginFail.class);
    }

    @Test
    void Basic헤더가_없으면_로그인_실패한다() {
        assertThatThrownBy(() -> loginService.login("Bearer token"))
                .isInstanceOf(AuthenticationException.Unauthorized.class);
    }

    @Test
    public void testLoginWithInvalidAuthorizationHeader() {
        // Verifying that the AuthenticationException.Unauthorized exception is thrown
        assertThrows(AuthenticationException.Unauthorized.class,
                () -> loginService.login("Bearer token"));

        // Verifying that the memberRepository.findByEmail() method is not called
        verify(memberRepository, never()).findByEmail(anyString());
    }
}
