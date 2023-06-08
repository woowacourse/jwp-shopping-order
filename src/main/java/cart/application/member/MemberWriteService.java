package cart.application.member;

import cart.domain.member.Member;
import cart.domain.repository.member.MemberRepository;
import cart.ui.member.dto.MemberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;

    public MemberWriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long createMember(MemberRequest memberRequest) {
        Member member = toMemberDomain(memberRequest);
        return memberRepository.createMember(member);
    }

    private Member toMemberDomain(MemberRequest memberRequest) {
        return new Member(null, memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword());
    }

}
