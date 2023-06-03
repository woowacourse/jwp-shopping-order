package cart.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.exception.IllegalOrderException;
import cart.repository.OrderRepository;

@Service
public class CancelOrderService {

    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;

    public CancelOrderService(ApplicationEventPublisher publisher, OrderRepository orderRepository) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void cancelOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        validateOrderBelongsToMember(member, order);
        if (order.getOrderStatus().canNotCancel()) {
            throw new IllegalOrderException(OrderStatus.PENDING.getDisplayName() + " 상태인 주문만 취소가 가능합니다");
        }
        handlePointRetrieval(order);
        orderRepository.updateStatus(order.cancel());
    }

    private void validateOrderBelongsToMember(Member member, Order order) {
        if (order == null || !member.equals(order.getMember())) {
            throw new IllegalOrderException.IllegalMember();
        }
    }

    private void handlePointRetrieval(Order order) {
        publisher.publishEvent(new PointRetrieveEvent(order.getId()));
    }
}
