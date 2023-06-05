package cart.application;

import cart.Repository.PointRepository;
import cart.domain.Member.Member;
import cart.domain.Point;
import cart.domain.Product.Price;
import cart.dto.OrderPointResponse;
import cart.dto.UserPointResponse;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public UserPointResponse findByMemberId(Long memberId) {
        Point point = pointRepository.getPointByMemberId(memberId);
        return UserPointResponse.of(point);
    }

    public void findPointByMember(Member member, Point usePoint, Price totalPrice) {
        Point memberPoint = pointRepository.getPointByMemberId(member.getId());
        validateIsUsable(memberPoint, usePoint, totalPrice);
    }

    private void validateIsUsable(Point memberPoint, Point usePoint, Price totalPrice) {
        if(memberPoint.isSmallerThan(usePoint)){
            throw new IllegalArgumentException("보유한 포인트보다 많은 포인트는 사용할 수 없습니다.");
        }

        if(memberPoint.isBiggerThan(totalPrice)){
            throw new IllegalArgumentException("주문금액보다 높은 포인트는 사용할 수 없습니다.");
        }
    }


    public void savePoint(Point usePoint, Price totalPrice, Long orderId, Long memberId) {
        Price realPrice = totalPrice.subtract(usePoint);

        Point memberPoint = pointRepository.getPointByMemberId(memberId);
        Point savePoint = Point.makePointFrom(realPrice);
        Point newPoint = memberPoint.getNewPoint(savePoint, usePoint);

        pointRepository.updatePoint(memberId, newPoint);
        pointRepository.savePointHistory(usePoint, savePoint, orderId, memberId);
    }

    public OrderPointResponse findSavedPointByOrderId(Long orderId) {
        Point savedPoint = pointRepository.findSavedPointByOrderId(orderId);
        return OrderPointResponse.of(savedPoint);
    }
}
