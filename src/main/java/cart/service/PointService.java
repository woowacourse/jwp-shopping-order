package cart.service;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
import cart.domain.PointEarningPolicy;
import cart.dto.PointResponse;
import cart.dto.SavingPointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PointService {
    private final MemberDao memberDao;

    public PointService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public PointResponse findUserPoints(Member member) {
        Point point = memberDao.findPoints(member);
        return PointResponse.of(point);
    }

    @Transactional(readOnly = true)
    public SavingPointResponse findSavingPoints(Long totalPrice) {
        Point point = PointEarningPolicy.calculateSavingPoints(totalPrice);
        return SavingPointResponse.of(point);
    }
}
