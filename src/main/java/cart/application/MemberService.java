package cart.application;

import cart.Repository.MemberRepository;
import cart.domain.Member.Email;
import cart.domain.Member.Member;
import cart.dto.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse getMemberById(final Long id) {
        final Member member = memberRepository.getMemberById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse getMemberByEmail(final String email) {
        final Member member = memberRepository.getMemberByEmail(new Email(email));
        return MemberResponse.of(member);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.getAllMembers()
                .stream()
                .map(MemberResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
