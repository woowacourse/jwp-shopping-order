package cart.application.service.member;

import cart.application.repository.member.MemberRepository;
import cart.application.service.member.dto.MemberResultDto;
import cart.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberReadService {

    private final MemberRepository memberRepository;

    public MemberReadService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResultDto> findAllMembers() {
        List<Member> members = memberRepository.findAllMembers();
        return members.stream()
                .map(MemberResultDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public MemberResultDto findMemberById(final Long id) {
        final Member member = memberRepository.getById(id);

        return MemberResultDto.from(member);
    }

    public MemberResultDto findMemberByEmail(final String email) {
        final Member member = memberRepository.getByEmail(email);

        return MemberResultDto.from(member);
    }
}
