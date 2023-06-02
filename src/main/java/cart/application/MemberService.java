package cart.application;

import cart.db.repository.MemberRepository;
import cart.domain.member.Member;
import cart.dto.login.MemberRequest;
import cart.dto.login.TokenResponse;
import cart.exception.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new AuthenticationException("중복된 아이디입니다. 다른 아이디를 입력해주세요.");
        }
        Long memberId = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getPassword()));
        Member newMember = new Member(memberId, memberRequest.getName(), memberRequest.getPassword());
        memberCouponService.add(new Member(memberId, newMember.getName(), newMember.getPassword()), 1L);
    }

    public TokenResponse generateMemberToken(final MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getName(), memberRequest.getPassword());
        if (!memberRepository.existsByMember(member)) {
            throw new AuthenticationException("아이디/비밀번호가 일치하지 않습니다.");
        }
        return new TokenResponse(member.generateToken());
    }
}
