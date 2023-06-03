package cart.domain.member.application;

import cart.domain.member.application.dto.MemberCashChargeRequest;
import cart.domain.member.application.dto.MemberCashChargeResponse;
import cart.domain.member.application.dto.MemberShowCurrentCashResponse;
import cart.domain.member.domain.Member;
import cart.domain.member.persistence.MemberDao;
import cart.global.config.AuthMember;
import cart.global.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void checkMemberExistByMemberInfo(String email, String password) {
        if (memberDao.isNotExistByEmailAndPassword(email, password)) {
            throw new AuthenticationException("인증된 사용자가 아닙니다.");
        }
    }

    public MemberCashChargeResponse chargeCash(AuthMember authMember, MemberCashChargeRequest memberCashChargeRequest) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        Member chargedMember = findMember.chargeCash(memberCashChargeRequest.getCashToCharge());
        memberDao.updateMemberCash(chargedMember);
        return new MemberCashChargeResponse(chargedMember.getCash());
    }

    public MemberShowCurrentCashResponse findMemberCurrentCharge(AuthMember authMember) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        return new MemberShowCurrentCashResponse(findMember.getCash());
    }
}
