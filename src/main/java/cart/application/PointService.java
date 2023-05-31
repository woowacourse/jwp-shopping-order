package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.PointResponse;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final MemberDao memberDao;

    public PointService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public PointResponse findPointByMemberId(Member member) {
        return PointResponse.from(memberDao.getMemberById(member.getId()).getPoint());
    }
}
