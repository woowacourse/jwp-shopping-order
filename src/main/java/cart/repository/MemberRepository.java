package cart.repository;

import cart.dao.MemberDao;
import cart.dao.PointDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import cart.entity.PointEntity;
import cart.exception.NoSuchMemberException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final PointDao pointDao;

    public MemberRepository(final MemberDao memberDao, final PointDao pointDao) {
        this.memberDao = memberDao;
        this.pointDao = pointDao;
    }

    public Member addMember(final Member member, final int joinEventPoint) {
        final MemberEntity savedMember = memberDao.addMember(MemberEntity.from(member));
        pointDao.insert(new PointEntity(savedMember.getId(), joinEventPoint));
        return savedMember.toMember();
    }

    public int findPointOf(final Member member) {
        return pointDao.findByMemberId(member.getId());
    }

    public Member getMemberByEmail(final String email) {
        final Optional<MemberEntity> member = memberDao.getMemberByEmail(email);
        if (member.isEmpty()) {
            throw new NoSuchMemberException("존재하지 않는 이메일입니다.");
        }
        return member.get().toMember();
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
        return memberDao.getAllMembers().stream()
                .map(MemberEntity::toMember)
                .collect(Collectors.toList());
    }

    public Member findMemberById(final Long id) {
        return memberDao.getMemberById(id).toMember();
    }
}
