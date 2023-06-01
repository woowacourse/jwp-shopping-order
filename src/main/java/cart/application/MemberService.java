package cart.application;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.infrastructure.AuthInfo;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member checkLoginMember(final AuthInfo authInfo) {
        final Member member = memberRepository.findByEmail(authInfo.getEmail())
                .orElseThrow(AuthenticationException::new);
        if (!member.checkPassword(authInfo.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }
}
