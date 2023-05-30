package cart.repository;

import cart.dao.MemberDao;
import cart.dao.PointDao;
import cart.domain.Member;
import cart.entity.PointEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public int findPointOf(final Member member) {
        return pointDao.findByMemberId(member.getId());
    }

    public void addPoint(final Member member, final int addedPoint) {
        final int currentPoint = pointDao.findByMemberId(member.getId());
        final int newPoint = currentPoint + addedPoint;
        updatePoint(member, newPoint);
    }

    public void updatePoint(final Member member, final int newPoint) {
        pointDao.updatePoints(new PointEntity(member.getId(), newPoint));
    }

    public List<Member> findAllMembers() {
        return memberDao.getAllMembers();
    }

    public Member findMemberById(final Long id) {
        return memberDao.getMemberById(id);
    }
}
