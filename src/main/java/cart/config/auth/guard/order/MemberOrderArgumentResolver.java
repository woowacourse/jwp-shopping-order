package cart.config.auth.guard.order;

import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberOrderArgumentResolver implements HandlerMethodArgumentResolver {

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

        Member member = memberRepository.findByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        Coupons coupons = couponRepository.findAllByMemberId(member.getId());
        member.initCoupons(coupons);

        return member;
    }
}
