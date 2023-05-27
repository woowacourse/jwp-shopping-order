package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dto.MemberDto;
import cart.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberDto::from)
                .collect(toUnmodifiableList());
    }
}
