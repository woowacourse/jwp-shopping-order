package cart.service;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.getAllMembers();
    }

    public Member getMemberByEmail(final Email email) {
        return memberRepository.getMemberByEmail(email);
    }
}
