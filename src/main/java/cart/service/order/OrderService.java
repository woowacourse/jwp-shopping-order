package cart.service.order;

import cart.domain.member.Member;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional(readOnly = true)
    public OrdersResponse findOrders(final Member member) {
        return null;
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(final Member member, final Long orderId) {
        return null;
    }
}
