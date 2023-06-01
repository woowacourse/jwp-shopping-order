package cart.application;

import cart.application.dto.response.MemberResponse;
import cart.application.dto.response.PointResponse;
import cart.domain.member.Member;
import cart.persistence.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAllMembers().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PointResponse findPointByMember(final Member member) {
        return new PointResponse(
                memberRepository.findMemberById(member.getId()).getPoint()
        );
    }
}
