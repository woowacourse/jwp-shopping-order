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
}
