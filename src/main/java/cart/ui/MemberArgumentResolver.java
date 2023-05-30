package cart.ui;

import cart.exception.AuthenticationException;
import cart.dao.MemberDao;
import cart.domain.Member;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;

    public MemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    //parameter 객체의 getParameterType()을 통해 controller method의 parameter가 Member class인지 확인
    //그리고 일치 여부를 boolean type으로 반환
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    //basic을 사용하여 member class의 email과 password를 뽑아옴
    //본인 여부 확인
    //최종적으로 member를 생성해서 반환
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인
        Member member = memberDao.getMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        return member;
    }
}
