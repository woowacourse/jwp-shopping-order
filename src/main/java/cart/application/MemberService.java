package cart.application;

import org.springframework.stereotype.Service;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.NoSuchMemberException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberBy(Long id) {
        return memberDao.getMemberById(id)
                .orElseThrow(NoSuchMemberException::new);
    }
}
