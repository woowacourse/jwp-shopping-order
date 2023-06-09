package cart.service;

import cart.domain.Member;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findById(final Long id){
       return memberRepository.findById(id);
    }
}
