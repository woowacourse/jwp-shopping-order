package cart.application;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberDao memberDao;

    public AuthService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void validateMember(final String email, final String password) {
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
