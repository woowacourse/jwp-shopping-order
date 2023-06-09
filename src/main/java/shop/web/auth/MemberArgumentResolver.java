package shop.web.auth;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int INDEX_OF_NAME = 0;
    private static final String DELIMITER = ":";

    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        BasicAuthorizationExtractor extractor = BasicAuthorizationExtractor.getInstance();

        String extractedHeader = extractor.extract(webRequest);
        String[] credentials = extractedHeader.split(DELIMITER);

        return memberRepository.findByName(credentials[INDEX_OF_NAME]);
    }
}
