package cart.application;

import static cart.application.mapper.MemberMapper.convertMember;
import static cart.application.mapper.MemberMapper.convertMemberResponse;

import cart.application.dto.member.MemberCouponResponse;
import cart.application.dto.member.MemberJoinRequest;
import cart.application.dto.member.MemberLoginRequest;
import cart.application.dto.member.MemberLoginResponse;
import cart.application.dto.member.MemberResponse;
import cart.application.mapper.MemberMapper;
import cart.common.auth.BasicTokenProvider;
import cart.domain.event.JoinMemberCouponEvent;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberRepository;
import cart.domain.member.MemberWithId;
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
    public long save(final MemberJoinRequest memberJoinRequest) {
        if (memberRepository.existByName(memberJoinRequest.getName())) {
            throw new BadRequestException(ErrorCode.MEMBER_DUPLICATE_NAME);
        }
        final Member member = convertMember(memberJoinRequest);
        final long savedMemberId = memberRepository.insert(member);
        applicationEventPublisher.publishEvent(new JoinMemberCouponEvent(savedMemberId));
        return savedMemberId;
    }

    public MemberLoginResponse login(final MemberLoginRequest memberLoginRequest) {
        final String name = memberLoginRequest.getName();
        final String password = memberLoginRequest.getPassword();
        validatePassword(name, password);
        final String basicToken = BasicTokenProvider.createToken(name, password);
        return new MemberLoginResponse(basicToken);
    }

    public MemberResponse getById(final Long id) {
        final MemberWithId member = memberRepository.findById(id);
        return convertMemberResponse(member);
    }

    public MemberResponse getByName(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        return MemberMapper.convertMemberResponse(member);
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
            .map(MemberMapper::convertMemberResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    private void validatePassword(final String name, final String password) {
        final Member member = memberRepository.findByName(name);
        final String encodedPassword = SHA256Service.encrypt(password);
        final EncryptedPassword requestEncryptedPassword = EncryptedPassword.create(encodedPassword);
        if (!requestEncryptedPassword.equals(member.memberPassword())) {
            throw new BadRequestException(ErrorCode.MEMBER_PASSWORD_INVALID);
        }
    }

    public List<MemberCouponResponse> getByCoupons(final String memberName) {
        final Member member = memberRepository.findMyCouponsByName(memberName);
        final List<MemberCoupon> memberCoupons = member.memberCoupons();
        return memberCoupons.stream()
            .map(MemberMapper::convertMemberCouponResponse)
            .collect(Collectors.toUnmodifiableList());
    }
}
