package cart.application;

import cart.domain.Member;
import cart.domain.MemberRepository;
import cart.exception.AuthenticationException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAllMembers();
    }

    public Member findAuthorizedMember(final String email, final String password) {
        final Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow();
        if (member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        return member;
    }
}
