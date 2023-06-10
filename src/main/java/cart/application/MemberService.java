package cart.application;

import cart.domain.member.Member;
import cart.domain.repository.JdbcMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;

    public MemberService(JdbcMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMember(Member member) {
        return memberRepository.getMemberById(member.getId());
    }
}
