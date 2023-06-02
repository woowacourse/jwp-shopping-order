package cart.application;

import cart.application.response.QueryMemberResponse;
import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class QueryMemberService {

    private final MemberRepository memberRepository;

    public QueryMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public QueryMemberResponse findByMemberId(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        return QueryMemberResponse.from(member);
    }
}
