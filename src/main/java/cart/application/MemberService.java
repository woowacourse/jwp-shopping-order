package cart.application;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberPoint getPoint(final Member member) {
        return memberRepository.findByEmail(member.getEmailValue())
                .getPoint();
    }
}
