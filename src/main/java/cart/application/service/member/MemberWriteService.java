package cart.application.service.member;

import cart.domain.member.Member;
import cart.domain.repository.member.MemberRepository;
import cart.ui.member.dto.MemberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberWriteService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public Long createMember(MemberRequest memberRequest) {
        Member member = memberMapper.toMember(memberRequest);
        return memberRepository.createMember(member);
    }

}
