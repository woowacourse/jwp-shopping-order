package cart.service;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.response.PointResponse;
import cart.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointService {

    private final MemberRepository memberRepository;

    public PointResponse getUserPoint(final Member member) {
        Member foundMember = memberRepository.findById(member.getId());

        return new PointResponse(foundMember.getPointValue(), Point.MIN_USAGE_VALUE);
    }
}
