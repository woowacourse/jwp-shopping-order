package cart.application;

import cart.domain.Member;
import cart.dto.MemberCreateResponse;
import cart.dto.MemberPointQueryResponse;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberCreateResponse addMember(final Member member) {
        return MemberCreateResponse.from(memberRepository.save(member));
    }

    public MemberPointQueryResponse findPointsOf(final Member member) {
        return new MemberPointQueryResponse(memberRepository.findPointsOf(member));
    }

    public void addPoints(final Member member, final int points) {
        memberRepository.addPoints(member, points);
    }
}
