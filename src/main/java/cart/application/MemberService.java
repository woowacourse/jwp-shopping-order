package cart.application;

import cart.domain.Member;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    
    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Transactional(readOnly = true)
    public Long getCurrentPoint(final Member member) {
        final Member memberByEmail = memberRepository.getMemberByEmail(member.getEmail());
        return memberByEmail.getPoint();
    }
}
