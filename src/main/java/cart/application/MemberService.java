package cart.application;

import cart.db.repository.MemberRepository;
import cart.domain.member.*;
import cart.dto.login.MemberRequest;
import cart.dto.login.TokenResponse;
import cart.exception.AuthenticationException;
import cart.util.BasicTokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cart.exception.ErrorCode.DUPLICATED_NAME;
import static cart.exception.ErrorCode.NOT_AUTHENTICATION_MEMBER;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private static final Long CONGRATULATION_COUPON_ID_TO_NEW_MEMBER = 1L;

    private final MemberCouponService memberCouponService;
    private final MemberRepository memberRepository;

    public MemberService(final MemberCouponService memberCouponService, final MemberRepository memberRepository) {
        this.memberCouponService = memberCouponService;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void add(final MemberRequest memberRequest) {
        if (memberRepository.existsByName(memberRequest.getName())) {
            throw new AuthenticationException(DUPLICATED_NAME);
        }

        Password encryptedPassword = Password.encrypt(memberRequest.getPassword());
        Long memberId = memberRepository.save(new Member(memberRequest.getName(), encryptedPassword));
        Member newMember = new Member(memberId, memberRequest.getName(), memberRequest.getPassword());
        memberCouponService.add(newMember, CONGRATULATION_COUPON_ID_TO_NEW_MEMBER);
    }

    public TokenResponse generateMemberToken(final MemberRequest memberRequest) {
        Password encryptedPassword = Password.encrypt(memberRequest.getPassword());
        Member member = new Member(memberRequest.getName(), encryptedPassword);
        if (!memberRepository.existsByMember(member)) {
            throw new AuthenticationException(NOT_AUTHENTICATION_MEMBER);
        }
        return new TokenResponse(BasicTokenGenerator.generate(member.getName(), member.getPassword()));
    }
}
