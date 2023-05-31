package cart.application;

import cart.application.repository.MemberRepository;
import cart.application.domain.Member;
import cart.presentation.dto.response.PointResponse;
import cart.application.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PointResponse getPoint(Member member) {
        return new PointResponse(member.getPoint());
    }

    public void validateMemberProfile(String email, String password) {
        Member member = getMemberByEmail(email);
        if (!member.hasPassword(password)) {
            throw new AuthenticationException();
        }
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 멤버는 존재하지 않습니다."));
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
