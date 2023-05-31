package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }
}
