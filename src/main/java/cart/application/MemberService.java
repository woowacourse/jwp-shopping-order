package cart.application;

import cart.domain.Member;
import cart.dto.response.MemberResponse;
import cart.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAll() {
        final List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberResponse::from).collect(Collectors.toList());
    }
}
