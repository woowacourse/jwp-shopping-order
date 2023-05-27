package cart.application;

import cart.dao.MemberDao;
import cart.exception.AuthenticationException;
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
}
