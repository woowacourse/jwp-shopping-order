package cart.application;

import cart.dao.PointDao;
import cart.domain.Member;
import cart.dto.response.PointResponse;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final PointDao pointDao;

    public PointService(final PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public PointResponse findByMember(final Member member) {
        return new PointResponse(pointDao.selectByMemberId(member.getId()));
    }
}
