package cart.authentication;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.exception.AuthenticationException.InvalidMember;
import cart.repository.MemberRepository;

public class AuthenticationMemberConverter {

    private final MemberRepository memberRepository;

    public AuthenticationMemberConverter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member convert(AuthInfo authInfo) {
        Member member = memberRepository.findByEmail(authInfo.getEmail())
                .orElseThrow(InvalidMember::new);

        if (!member.checkPassword(authInfo.getPassword())) {
            throw new AuthenticationException.InvalidMember();
        }

        return member;
    }
}
