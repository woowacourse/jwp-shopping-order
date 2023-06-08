package cart.application;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberDao memberDao;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public void validateMember(String email, String password) {
        Member member = memberDao.findMemberByEmail(email)
                .orElseThrow(() -> new CartException(ErrorCode.MEMBER_NOT_FOUND));

        if (isWrongPassword(member, password)) {
            throw new CartException(ErrorCode.AUTHENTICATION);
        }
    }

    private boolean isWrongPassword(Member member, String password) {
        return !member.checkPassword(password);
    }
}
