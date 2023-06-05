package cart.Repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.domain.Order.Order;
import cart.domain.Point;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import org.springframework.stereotype.Repository;

import static cart.Repository.mapper.PointMapper.toPointEntity;
import static cart.Repository.mapper.PointMapper.toPointHistoryEntity;

@Repository
public class PointRepository {

    private final PointDao pointDao;
    private final PointHistoryDao pointHistoryDao;


    public PointRepository(PointDao pointDao, PointHistoryDao pointHistoryDao) {
        this.pointDao = pointDao;
        this.pointHistoryDao = pointHistoryDao;
    }


    public Point getPointByMemberId(Long memberId) {
        PointEntity pointEntity = pointDao.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 포인트 입니다."));
        return new Point(pointEntity.getPoint());
    }

    public void savePointHistory(Point usePoint, Point savePoint, Long orderId, Long memberId) {

        pointHistoryDao.save(toPointHistoryEntity(usePoint, savePoint, memberId, orderId));
    }

    public Point findSavedPointByOrderId(Long orderId) {
        PointHistoryEntity pointHistoryEntity = pointHistoryDao.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문내역에 해당하는 포인트가 없습니다."));
        return new Point(pointHistoryEntity.getPointSaved());
    }

    public void updatePoint(Long memberId, Point newPoint) {
        pointDao.update(toPointEntity(memberId, newPoint));
    }
}
