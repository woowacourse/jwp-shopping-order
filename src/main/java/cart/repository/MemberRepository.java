package cart.repository;

import cart.dao.MemberDao;
import cart.dao.PointDao;
import cart.domain.Member;
import cart.entity.PointEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private static final int JOIN_EVENT_POINT = 5_000;
    private final MemberDao memberDao;
    private final PointDao pointDao;

    public MemberRepository(final MemberDao memberDao, final PointDao pointDao) {
        this.memberDao = memberDao;
        this.pointDao = pointDao;
    }

    public Member save(final Member member) {
        final Member savedMember = memberDao.addMember(member);
        pointDao.insert(new PointEntity(savedMember.getId(), JOIN_EVENT_POINT));
        return savedMember;
    }

    public int findPointsOf(final Member member) {
        return pointDao.findByMemberId(member.getId());
    }

    public void addPoints(final Member member, final int addedPoints) {
        final int currentPoints = pointDao.findByMemberId(member.getId());
        final int newPoints = currentPoints + addedPoints;
        pointDao.updatePoints(new PointEntity(member.getId(), newPoints));
    }
}
