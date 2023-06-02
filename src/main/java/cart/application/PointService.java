package cart.application;

import cart.dao.PointDao;
import cart.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PointService {

    private final PointDao pointDao;

    public PointService(final PointDao pointDao) {
        this.pointDao = pointDao;
    }

    @Transactional(readOnly = true)
    public long findByMember(final Member member) {
        return pointDao.selectByMemberId(member.getId());
    }

    public void updateByMember(final Member member, final long point) {
        pointDao.update(member.getId(), point);
    }
}
