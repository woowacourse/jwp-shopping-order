package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.MemberCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public void signUp(MemberCreateRequest request) {
        Member member = new Member(request.getEmail(), request.getPassword());
        memberDao.insert(member);
    }
}
