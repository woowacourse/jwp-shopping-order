package cart.config;

import cart.application.AuthService;
import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String BASIC = "Basic";

    private final MemberDao memberDao;

    public MemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String credential = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String authValueWithBase64Encoding = credential.substring(BASIC.length()).trim();
        String auth = new String(Base64Utils.decodeFromString(authValueWithBase64Encoding));

        String[] emailAndPasswordWithDecryption = auth.split(":");
        String email = emailAndPasswordWithDecryption[0];
        return memberDao.findMemberByEmail(email).orElseThrow(() -> new CartException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
