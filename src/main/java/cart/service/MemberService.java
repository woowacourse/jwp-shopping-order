package cart.service;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

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
