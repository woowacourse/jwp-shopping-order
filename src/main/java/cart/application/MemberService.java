package cart.application;

import cart.domain.member.Member;
import cart.repository.MemberRepository;
import cart.ui.controller.dto.response.MemberPointResponse;
import cart.ui.controller.dto.response.MemberResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        return MemberResponse.listOf(members);
    }

    public MemberResponse findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        return MemberResponse.from(member);
    }

    public MemberResponse findByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        return MemberResponse.from(member);
    }

    public MemberPointResponse getPoint(Member member) {
        Member renewMember = memberRepository.findById(member.getId());
        return new MemberPointResponse(renewMember.getPoint());
    }
}
