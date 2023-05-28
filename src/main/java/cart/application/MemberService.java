package cart.application;

import cart.application.dto.MemberLoginRequest;
import cart.application.dto.MemberSaveRequest;
import cart.application.dto.MemberResponse;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public long save(final MemberSaveRequest memberSaveRequest) {
        if (memberRepository.existByName(memberSaveRequest.getName())) {
            throw new BadRequestException(ErrorCode.MEMBER_DUPLICATE_NAME);
        }
        final Member member = Member.create(memberSaveRequest.getName(), memberSaveRequest.getPassword());
        return memberRepository.insert(member);
    }

    public MemberResponse getById(final Long id) {
        final Member member = memberRepository.findById(id);
        return new MemberResponse(id, member.getName(), member.getPassword());
    }

    public MemberResponse getByName(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        return new MemberResponse(member.getName(), member.getPassword());
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
            .map(memberWithId -> new MemberResponse(memberWithId.getId(), memberWithId.getMember().getName(),
                memberWithId.getMember().getPassword()))
            .collect(Collectors.toUnmodifiableList());
    }
}
