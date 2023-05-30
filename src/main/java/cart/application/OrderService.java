package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.GetOrdersResponse;
import cart.application.dto.OrderContents;
import cart.application.dto.PostOrderRequest;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Page;
import cart.domain.Paginator;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final Paginator<Order> paginator;

    public OrderService(OrderDao orderDao, Paginator<Order> paginator) {
        this.orderDao = orderDao;
        this.paginator = paginator;
    }

    @Transactional(readOnly = true)
    public GetOrdersResponse getOrdersWithPagination(Member member, GetOrdersRequest request) {
        List<Order> orders = orderDao.findAllByMemberId(member.getId());
        Page<Order> paginatedOrders = paginator.paginate(orders, request.getPage());
        List<OrderContents> contents = OrderContents.from(paginatedOrders.getContents());
        return new GetOrdersResponse(paginatedOrders.getTotalPages(), paginatedOrders.getCurrentPage(), paginatedOrders.getPageSize(),
            contents);
    }

    public GetDetailedOrderResponse getOrder(Member member, Long orderId) {
        return null;
    }

    public Long addOrder(Member member, PostOrderRequest request) {
        return null;
    }
}
