package cart.application;

import cart.Repository.PointRepository;
import cart.domain.Member.Member;
import cart.domain.Order.Order;
import cart.domain.Point;
import cart.domain.Product.Price;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point findByMemberId(Long memberId) {
        return pointRepository.getPointByMemberId(memberId);
    }

    public void checkUsePointLessThanUserPoint(Member member, Point usePoint, Price totalPrice) {
        Point memberPoint = findByMemberId(member.getId());
        memberPoint.validateIsSameOrBiggerThan(usePoint);

        if (usePoint.point() > totalPrice.price()) {
            throw new IllegalArgumentException("주문금액보다 높은 포인트는 사용할 수 없습니다.");
        }
    }


    public void savePoint(Point usePoint, Order order) {
        Price realPrice = order.getTotalPrice()
                .subtract(usePoint);
        Point savePoint = Point.makePointFrom(realPrice);
        pointRepository.update(usePoint, savePoint, order);
    }
}
