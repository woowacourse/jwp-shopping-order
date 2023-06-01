package cart.application;

import cart.domain.Member;
import cart.dto.PointResponse;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PointResponse getMemberPoint(Member member) {
        int pointOfMember = memberRepository.findPointByMember(member);

        return new PointResponse(pointOfMember);
    }
}
