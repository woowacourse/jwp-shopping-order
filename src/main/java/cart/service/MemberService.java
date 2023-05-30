package cart.service;

import cart.dto.MemberDto;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

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
