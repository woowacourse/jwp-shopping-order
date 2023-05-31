package cart.application.service;

import cart.application.repository.MemberRepository;
import cart.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;

    public MemberWriteService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long createMember(MemberRequest memberRequest) {
        Member member = new Member(memberRequest);
        return memberRepository.createMember(member);
    }

}
