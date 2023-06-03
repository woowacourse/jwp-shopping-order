package cart.application;

import cart.Repository.PointRepository;
import cart.domain.Member.Member;
import cart.domain.Order.Order;
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

    public void checkUsePointLessThanUserPoint(Member member, Point usePoint, Price totalPrice) {
        Point memberPoint = pointRepository.getPointByMemberId(member.getId());
        memberPoint.validateIsSameOrBiggerThan(usePoint);

        if (usePoint.point() > totalPrice.price()) {
            throw new IllegalArgumentException("주문금액보다 높은 포인트는 사용할 수 없습니다.");
        }
    }


    public void savePoint(Point usePoint, Order order) {

        Price realPrice = order.getTotalPrice()
                .subtract(usePoint);
        Point savePoint = Point.makePointFrom(realPrice);

        Point memberPoint = pointRepository.getPointByMemberId(order.getMember().getId());

        Point newPoint = memberPoint.getNewPoint(savePoint, usePoint);


        pointRepository.update(usePoint, savePoint, order, newPoint);
    }

    public OrderPointResponse findSavedPointByOrderId(Long orderId) {
        Point savedPoint = pointRepository.findSavedPointByOrderId(orderId);
        return OrderPointResponse.of(savedPoint);
    }
}
