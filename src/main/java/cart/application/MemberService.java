package cart.application;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.dto.PointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public PointResponse getPoint(final Member member) {
        final Member findMember = memberDao.getMemberByEmail(member.getEmailValue());

        return new PointResponse(findMember.getPointValue());
    }
}
