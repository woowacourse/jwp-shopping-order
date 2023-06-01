package cart.config.auth.guard.order;

import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberOrderArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_HEADER = "basic";
    private static final String AUTHORIZATION_SEPARATOR = " ";
    private static final String CREDENTIAL_SEPARATOR = ":";

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    public MemberOrderArgumentResolver(final MemberRepository memberRepository, final CouponRepository couponRepository) {
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OrderAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateNullAuthorization(authorization);

        String[] authHeader = authorization.split(AUTHORIZATION_SEPARATOR);

        validateHeader(authHeader);

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(CREDENTIAL_SEPARATOR);
        String email = credentials[0];
        String password = credentials[1];
        Member member = memberRepository.findByEmail(email);

        validateMember(password, member);

        Coupons coupons = couponRepository.findAllByMemberId(member.getId());
        member.initCoupons(coupons);

        return member;
    }

    private void validateNullAuthorization(final String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }
    }

    private void validateHeader(final String[] authHeader) {
        if (!authHeader[0].equalsIgnoreCase(BASIC_HEADER)) {
            throw new AuthenticationException();
        }
    }

    private void validateMember(final String password, final Member member) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
