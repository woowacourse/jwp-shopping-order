package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.GetOrdersResponse;
import cart.application.dto.OrderContents;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Page;
import cart.domain.Paginator;
import cart.exception.AuthenticationException;
import cart.repository.OrderRepository;

@Service
public class FindOrderService {

    private final Paginator<Order> paginator;
    private final PointService pointService;
    private final OrderRepository orderRepository;

    public FindOrderService(Paginator<Order> paginator, PointService pointService, OrderRepository orderRepository) {
        this.paginator = paginator;
        this.pointService = pointService;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public GetOrdersResponse getOrdersWithPagination(Member member, GetOrdersRequest request) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        Page<Order> paginatedOrders = paginator.paginate(orders, request.getPage());
        List<OrderContents> contents = OrderContents.from(paginatedOrders.getContents());
        return new GetOrdersResponse(paginatedOrders.getTotalPages(), paginatedOrders.getCurrentPage(), paginatedOrders.getPageSize(),
            contents);
    }

    @Transactional(readOnly = true)
    public GetDetailedOrderResponse getOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (member.equals(order.getMember())) {
            int increasedPoint = pointService.getIncreasedPointAmountByMemberId(member.getId());
            int usedPoint = pointService.getUsedPointAmountByMemberId(member.getId());
            return GetDetailedOrderResponse.from(order, increasedPoint, usedPoint);
        }
        throw new AuthenticationException();
    }

    @Transactional
    public void cancelOrder(Member member, Long orderId) {
        // // 주문 취소
        // Order order = orderDao.findById(orderId);
        // // 1. 해당 주문자가 신청한 주문인지 검증한다
        // if (order.getMember() != member) {
        //     throw new IllegalOrderException("올바르지 않은 요청입니다");
        // }
        // // 2. 주문의 상태가 삭제 가능한지 검증한다
        // if (order.getOrderStatus().canNotCancel()) {
        //     throw new IllegalOrderException(OrderStatus.PENDING.getDisplayName() + " 상태인 주문만 취소가 가능합니다");
        // }
        // // 3. 포인트를 반환한다
        // // TODO: 포인트 유효기간
        // int savedPoint = order.getSavedPoint();
        // int usedPoint = order.getUsedPoint();
        // int difference = usedPoint - savedPoint;
        // if (difference != 0) {
        //     Point originalPoint = pointDao.findByMemberId(member.getId());
        //     Point changedPoint = originalPoint.changeBy(difference);
        //     pointDao.update(changedPoint);
        // }
        // // 4. 주문의 상태를 변경한다
        // orderDao.update(order.cancel());
    }
}
