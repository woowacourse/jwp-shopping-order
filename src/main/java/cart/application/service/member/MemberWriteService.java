package cart.application.service.member;

import cart.application.repository.member.MemberRepository;
import cart.domain.member.Member;
import cart.ui.member.dto.MemberRequest;
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
        final Member member = new Member(memberRequest);
        return memberRepository.createMember(member);
    }

}
