package cart.application;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.Point;
import cart.exception.IllegalPointException;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public void savePointOfOrder(int requestedPoint, Order order) {
        Point pointToUse = new Point(requestedPoint);
        checkPointAbleToUse(pointToUse, order);
        savePointHistory(pointToUse, order);
    }

    private void checkPointAbleToUse(Point pointToUse, Order order) {
        checkMemberPointAvailability(pointToUse, order.getMemberId());
        checkOrderPointAvailability(pointToUse, order.calculateTotalPrice());
    }

    private void checkMemberPointAvailability(Point pointToUse, long memberId) {
        Point memberPoint = pointRepository.findByMemberId(memberId);
        if (pointToUse.isGreaterThan(memberPoint)) {
            throw new IllegalPointException("보유한 포인트보다 많은 포인트를 사용할 수 없습니다");
        }
    }

    private void checkOrderPointAvailability(Point pointToUse, Money orderPrice) {
        Money pointToMoney = pointToUse.toMoney();
        if (pointToMoney.isGreaterThan(orderPrice)) {
            throw new IllegalPointException("상품 금액보다 큰 포인트는 사용할 수 없습니다.");
        }
    }

    private void savePointHistory(Point pointToUse, Order order) {
        Money paymentPrice = order.calculateTotalPrice().subtract(pointToUse.toMoney());
        Point pointToSave = Point.fromMoney(paymentPrice);
        Point updatedPoint = calculateUpdatedPoint(order.getMemberId(), pointToUse, pointToSave);

        pointRepository.update(updatedPoint, order.getMemberId());
        pointRepository.savePointHistory(pointToSave, pointToUse, order.getId(),
            order.getMemberId());
    }

    private Point calculateUpdatedPoint(long memberId, Point pointToUse, Point pointToSave) {
        return pointRepository.findByMemberId(memberId)
            .subtract(pointToUse)
            .add(pointToSave);
    }

    @Transactional(readOnly = true)
    public Point findSavedPointByOrderId(long orderId) {
        return pointRepository.findSavedPointByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public Point findPointOfMember(Member member) {
        return pointRepository.findByMemberId(member.getId());
    }

}
