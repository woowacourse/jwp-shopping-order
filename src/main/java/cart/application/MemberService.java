package cart.application;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.dto.MemberRequest;
import cart.dto.TokenResponse;
import cart.exception.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberCouponService memberCouponService;
    private final MemberDao memberDao;

    public MemberService(final MemberCouponService memberCouponService, final MemberDao memberDao) {
        this.memberCouponService = memberCouponService;
        this.memberDao = memberDao;
    }

    @Transactional
    public void add(final MemberRequest memberRequest) {
        Long memberId = memberDao.addMember(new Member(memberRequest.getName(), memberRequest.getPassword()));
        Member newMember = new Member(memberId, memberRequest.getName(), memberRequest.getPassword());
        memberCouponService.add(new Member(memberId, newMember.getName(), newMember.getPassword()), 1L);
    }

    public TokenResponse generateMemberToken(final MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getName(), memberRequest.getPassword());
        if (!memberDao.existsByMember(member)) {
            throw new AuthenticationException("아이디/비밀번호가 일치하지 않습니다.");
        }
        return new TokenResponse(member.generateToken());
    }
}
