package cart.application;

import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMember(Member member) {
        return memberRepository.getMemberById(member.getId());
    }
}
