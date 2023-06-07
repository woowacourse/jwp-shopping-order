package cart.application.service;

import cart.application.exception.MemberNotFoundException;
import cart.application.repository.MemberRepository;
import cart.entity.Member;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.response.PointResponse;
import cart.application.exception.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PointResponse getPoint(AuthInfo authInfo) {
        Member member = getMemberByEmail(authInfo.getEmail());
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
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
