package cart.application;

import cart.application.event.UpdateMemberPointEvent;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.point.Point;
import cart.domain.product.Price;
import cart.dto.response.PointHistoryResponse;
import cart.dto.response.PointResponse;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
public class PointService {

    private final PointRepository pointRepository;
    private final OrderRepository orderRepository;

    public PointService(final PointRepository pointRepository, final OrderRepository orderRepository) {
        this.pointRepository = pointRepository;
        this.orderRepository = orderRepository;
    }

    @TransactionalEventListener
    public void updateMemberPoint(final UpdateMemberPointEvent updateRequest) {
        final Member member = updateRequest.getMember();
        final Order order = orderRepository.findByOrderId(updateRequest.getOrderId());

        order.checkOwner(member);

        final Price orderPrice = order.getTotalPrice();

        final Point usePoint = new Point(updateRequest.getOrderRequest().getUsePoint());
        final Point pointByMember = pointRepository.findPointByMember(member);

        final Price memberPaidPrice = new Price(orderPrice.price() - usePoint.getPoint());
        final Point earnPoint = Point.calculateFromPrice(memberPaidPrice);

        final Point updatedMemberPoint = pointByMember.subtract(usePoint).add(earnPoint);

        pointRepository.updateMemberPoint(member, updatedMemberPoint);
        pointRepository.updatePointHistory(order, usePoint, earnPoint);
    }

    @Transactional(readOnly = true)
    public PointResponse findPointByMember(final Member member) {
        final Point pointByMember = pointRepository.findPointByMember(member);
        return new PointResponse(pointByMember.getPoint());
    }

    @Transactional(readOnly = true)
    public PointHistoryResponse findPointHistory(final Long orderId) {
        final Point savedPointByOrder = pointRepository.findPointSavedHistory(orderId);

        return new PointHistoryResponse(savedPointByOrder.getPoint());
    }
}
