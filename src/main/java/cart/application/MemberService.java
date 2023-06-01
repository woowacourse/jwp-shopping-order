package cart.application;

import cart.domain.member.Member;
import cart.repository.MemberRepository;
import cart.ui.controller.dto.response.MemberPointResponse;
import cart.ui.controller.dto.response.MemberResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.getAllMembers();
        return members.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberByEmail(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return MemberResponse.from(member);
    }

    public MemberResponse getMemberByEmailAndPassword(String email, String password) {
        Member member = memberRepository.getMemberByEmailAndPassword(email, password);
        return MemberResponse.from(member);
    }

    public MemberPointResponse getMemberPoint(Member member) {
        Member renewMember = memberRepository.getMemberById(member.getId());
        return new MemberPointResponse(renewMember.getPoint());
    }
}
