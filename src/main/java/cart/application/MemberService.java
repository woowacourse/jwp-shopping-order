package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.Member;
import cart.dto.MemberResponse;
import cart.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        final List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
