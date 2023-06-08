package cart.common.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cart.application.MemberService;
import cart.application.dto.member.MemberResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.ServletWebRequest;

@ExtendWith(MockitoExtension.class)
class MemberNameArgumentResolverTest {

    @Mock
    private ServletWebRequest servletWebRequest;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberNameArgumentResolver memberNameArgumentResolver;

    class MemberNameArgumentResolverTestController {

        void hasParam(@MemberName final String memberName) {
        }

        void hasNotParam(final String memberName) {
        }
    }

    @ParameterizedTest(name = "주어진 파라미터가 MemberName 어노테이션을 지원하는지 확인한다.")
    @CsvSource(value = {"hasParam:true", "hasNotParam:false"}, delimiter = ':')
    void supportsParameter(final String methodName, final boolean isSupport)
        throws NoSuchMethodException {
        // given
        final Method method = MemberNameArgumentResolverTestController.class.getDeclaredMethod(methodName,
            String.class);
        final MethodParameter memberEmailParam = MethodParameter.forExecutable(method, 0);

        // when, then
        assertThat(memberNameArgumentResolver.supportsParameter(memberEmailParam))
            .isSameAs(isSupport);
    }

    @Test
    @DisplayName("요청의 Authorization 헤더 정보를 바탕으로 사용자 이름을 추출한다.")
    void resolveArgument() throws NoSuchMethodException {
        // given
        final Method method = MemberNameArgumentResolverTestController.class.getDeclaredMethod(
            "hasParam", String.class);
        final MethodParameter memberNameParam = MethodParameter.forExecutable(method, 0);
        when(servletWebRequest.getHeader("Authorization"))
            .thenReturn("Basic am91cm5leTp0ZXN0MTIzNA==");
        final MemberResponse memberResponse = new MemberResponse("journey",
            "937e8d5fbb48bd4949536cd65b8d35c426b80d2f830c5c308e2cdec422ae2244");
        when(memberService.getByName(any()))
            .thenReturn(memberResponse);

        // when
        final String memberName = memberNameArgumentResolver.resolveArgument(memberNameParam,
            null, servletWebRequest, null);

        // then
        assertThat(memberName)
            .isEqualTo("journey");
    }
}
