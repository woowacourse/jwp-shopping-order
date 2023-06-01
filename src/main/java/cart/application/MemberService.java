package cart.application;

import cart.domain.Member;
import cart.repository.MemberRepository;
import cart.ui.dto.MemberRankResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberRankResponse getMemberInfo(final Long id) {
        final Member member = memberRepository.findById(id);
        return new MemberRankResponse(
                member.getId(),
                member.getRank().name(),
                (int) (member.getRank().getDiscountRate() * 100)
        );
    }
}
