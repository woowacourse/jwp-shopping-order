package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderPage;
import cart.dto.OrderPageResponse;
import cart.dto.OrderResponse;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointRepository pointRepository;
    private final OrderPage orderPage;

    public OrderService(OrderRepository orderRepository, PointRepository pointRepository, OrderPage orderPage) {
        this.orderRepository = orderRepository;
        this.pointRepository = pointRepository;
        this.orderPage = orderPage;
    }

    public OrderPageResponse findOrders(Member member, int pageNumber) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        List<OrderResponse> orderResponses = orders.stream()
                .skip(orderPage.calculateSkipNumber(pageNumber))
                .limit(orderPage.getLimit())
                .map(order -> new OrderResponse(order.getId(), order.getPayAmount(), order.getOrderAt(),
                        order.getFirstItemName(), order.getFirstItemImageUrl(), order.getItemSize()))
                .collect(Collectors.toList());
        return new OrderPageResponse(orderPage.calculateTotalPages(orders.size()), pageNumber, orderResponses);
    }
}
