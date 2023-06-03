package cart.application.member;

import cart.domain.member.Member;
import cart.dto.member.MemberResponse;
import cart.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void update(final Member member, final int money) {
        member.update(money);
        memberRepository.update(member);
    }

    public MemberResponse findByEmail(final Member member) {
        Member findedMember = memberRepository.findByEmail(member.getEmail());
        return new MemberResponse(findedMember);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

}
