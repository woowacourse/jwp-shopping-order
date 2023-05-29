package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.PostOrderRequest;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Paginator;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final Paginator paginator;

    public OrderService(OrderDao orderDao, Paginator paginator) {
        this.orderDao = orderDao;
        this.paginator = paginator;
    }

    @Transactional(readOnly = true)
    public List<GetOrderResponse> getOrdersWithPagination(Member member, GetOrdersRequest request) {
        List<Order> orders = orderDao.findAllByMemberId(member.getId());
        List<Order> paginatedOrders = paginator.paginate(orders, request.getPage());
        return GetOrderResponse.from(paginatedOrders);
    }

    public GetDetailedOrderResponse getOrder(Member member, Long orderId) {
        return null;
    }

    public Long addOrder(Member member, PostOrderRequest request) {
        return null;
    }
}
