package cart.application;

import cart.db.repository.MemberRepository;
import cart.domain.member.Member;
import cart.dto.login.MemberRequest;
import cart.dto.login.TokenResponse;
import cart.exception.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cart.exception.ErrorCode.DUPLICATED_NAME;
import static cart.exception.ErrorCode.NOT_AUTHENTICATION_MEMBER;

@Service
public class MemberService {

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
        Long memberId = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getPassword()));
        Member newMember = new Member(memberId, memberRequest.getName(), memberRequest.getPassword());
        memberCouponService.add(new Member(memberId, newMember.getName(), newMember.getPassword()), 1L);
    }

    public TokenResponse generateMemberToken(final MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getName(), memberRequest.getPassword());
        if (!memberRepository.existsByMember(member)) {
            throw new AuthenticationException(NOT_AUTHENTICATION_MEMBER);
        }
        return new TokenResponse(member.generateToken());
    }
}
