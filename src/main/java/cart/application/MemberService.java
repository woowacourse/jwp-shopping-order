package cart.application;

import cart.application.repository.MemberRepository;
import cart.domain.Member;
import cart.dto.PointResponse;
import cart.exception.AuthenticationException;
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
        return memberRepository.findByEmail(email);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
