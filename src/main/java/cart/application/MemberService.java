package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    
    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    
    public Long getCurrentPoint(final Member member) {
        final Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        return memberByEmail.getPoint();
    }
}
