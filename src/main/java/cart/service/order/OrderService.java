package cart.service.order;

import cart.domain.member.Member;
import cart.dto.history.OrderHistory;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.exception.MemberNotOwnerException;
import cart.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrders(final Member member) {
        List<OrderHistory> orders = orderRepository.findAllByMemberId(member.getId());
        return OrdersResponse.from(orders);
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(final Member member, final Long orderId) {
        validateMemberHasOrder(member, orderId);

        OrderHistory order = orderRepository.findOrderHistory(orderId);
        return OrderResponse.from(order);
    }

    private void validateMemberHasOrder(final Member member, final Long orderId) {
        if (!orderRepository.isMemberOrder(member, orderId)) {
            throw new MemberNotOwnerException();
        }
    }
}
