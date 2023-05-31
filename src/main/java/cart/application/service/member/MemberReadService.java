package cart.application.service.member;

import cart.application.repository.MemberRepository;
import cart.application.service.member.dto.MemberResultDto;
import cart.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        final Member member = memberRepository.findMemberById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 존재하지 않습니다."));
        return MemberResultDto.from(member);
    }

    public MemberResultDto findMemberByEmail(final String email) {
        final Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 존재하지 않습니다."));
        return MemberResultDto.from(member);
    }

    public boolean isMemberExist(final String email, final String password) {
        return memberRepository.isMemberExist(email, password);
    }

}
