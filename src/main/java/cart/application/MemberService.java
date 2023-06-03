package cart.application;

import cart.domain.Member;
import cart.repository.MemberRepository;
import cart.ui.dto.MemberGradeResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberGradeResponse getMemberInfo(final Long id) {
        final Member member = memberRepository.findById(id);
        return new MemberGradeResponse(
                member.getId(),
                member.getGrade().name(),
                (int) (member.getGrade().getDiscountRate() * 100)
        );
    }
}
