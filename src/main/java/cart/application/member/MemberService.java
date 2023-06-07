package cart.application.member;

import cart.domain.member.Member;
import cart.dto.member.MemberResponse;
import cart.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateRank(final Member member, final int money) {
        member.update(money);
        memberRepository.update(member);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

}
