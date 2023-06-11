package cart.application;

import cart.domain.member.Member;
import cart.domain.repository.MemberRepository;
import cart.exception.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMember(Member member) {
        return memberRepository.getMemberById(member.getId())
                .orElseThrow(MemberException.InvalidIdByNull::new);
    }
}
