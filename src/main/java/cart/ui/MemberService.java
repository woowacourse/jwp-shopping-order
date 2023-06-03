package cart.ui;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void logIn(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        if (!memberByEmail.checkPassword(member.getPassword())) {
            throw new BadRequestException(ExceptionType.LOGIN_FAIL);
        }
    }
}
