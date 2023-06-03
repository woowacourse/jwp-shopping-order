package cart.ui;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;

    public MemberArgumentResolver(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new MissingRequestHeaderException(HttpHeaders.AUTHORIZATION, parameter);
        }

        final String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException.InvalidScheme();
        }
        if (authHeader.length != 2) {
            throw new AuthenticationException.InvalidCredentials();
        }

        return this.generateMember(authHeader);
    }

    private Member generateMember(final String[] authHeader) {
        return Optional.ofNullable(Base64.decodeBase64(authHeader[1]))
                .map(String::new)
                .map(decodedBytes -> decodedBytes.split(":"))
                .filter(credentials -> credentials.length == 2)
                .flatMap(this::getMember)
                .orElseThrow(AuthenticationException.InvalidCredentials::new);
    }

    private Optional<Member> getMember(final String[] credentials) {
        final Optional<Member> memberByEmail = this.memberDao.getMemberByEmail(credentials[0]);
        return memberByEmail.filter(member1 -> member1.checkPassword(credentials[1]));
    }
}
