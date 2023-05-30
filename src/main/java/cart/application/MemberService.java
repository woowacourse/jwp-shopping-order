package cart.application;

import cart.application.dto.coupon.CouponResponse;
import cart.application.dto.member.MemberLoginRequest;
import cart.application.dto.member.MemberLoginResponse;
import cart.application.dto.member.MemberResponse;
import cart.application.dto.member.MemberSaveRequest;
import cart.common.auth.BasicTokenProvider;
import cart.domain.coupon.CouponSaveEvent;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.security.SHA256Service;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MemberService(final MemberRepository memberRepository,
                         final ApplicationEventPublisher applicationEventPublisher) {
        this.memberRepository = memberRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public long save(final MemberSaveRequest memberSaveRequest) {
        if (memberRepository.existByName(memberSaveRequest.getName())) {
            throw new BadRequestException(ErrorCode.MEMBER_DUPLICATE_NAME);
        }
        final Member member = Member.create(memberSaveRequest.getName(), memberSaveRequest.getPassword());
        final long savedMemberId = memberRepository.insert(member);
        applicationEventPublisher.publishEvent(new CouponSaveEvent(savedMemberId));
        return savedMemberId;
    }

    public MemberLoginResponse login(final MemberLoginRequest memberLoginRequest) {
        final String name = memberLoginRequest.getName();
        final String password = memberLoginRequest.getPassword();

        final Member member = memberRepository.findByName(name);
        final String encodedPassword = SHA256Service.encrypt(password);

        if (!encodedPassword.equals(member.password())) {
            throw new BadRequestException(ErrorCode.MEMBER_PASSWORD_INVALID);
        }
        final String basicToken = BasicTokenProvider.createToken(name, password);
        return new MemberLoginResponse(basicToken);
    }

    public MemberResponse getById(final Long id) {
        final Member member = memberRepository.findById(id);
        return new MemberResponse(id, member.name(), member.password());
    }

    public MemberResponse getByName(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        return new MemberResponse(member.name(), member.password());
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
            .map(memberWithId -> new MemberResponse(memberWithId.getId(), memberWithId.getMember().name(),
                memberWithId.getMember().password()))
            .collect(Collectors.toUnmodifiableList());
    }
}
