package cart.application;

import cart.dao.MemberDao;
import cart.dao.PointDao;
import cart.domain.Member;
import cart.domain.Point;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final PointDao pointDao;

    public MemberService(MemberDao memberDao, PointDao pointDao) {
        this.memberDao = memberDao;
        this.pointDao = pointDao;
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }

    public Point getMemberPoint(Member member) {
        Point point = pointDao.findByMember(member)
                .orElseThrow(() -> new IllegalStateException("해당 member의 point 정보가 존재하지 않습니다. 확인해주세요."));
        return point;
    }
}
