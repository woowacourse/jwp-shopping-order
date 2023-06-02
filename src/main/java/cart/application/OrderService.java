package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.OrderException.EmptyItemInput;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // TODO: 예외 정리하기
    public Order createDraftOrder(Member member, List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            throw new EmptyItemInput();
        }
        List<OrderItem> orderItems = cartItemIds.stream()
                .map(cartItemId -> cartItemRepository.findById(cartItemId).orElseThrow())
                .map(OrderItem::from)
                .collect(Collectors.toList());
        return new Order(member, orderItems);
    }

    @Transactional
    public Long createOrderAndSave(Member member, List<Long> cartItemIds) {
        Order draftOrder = createDraftOrder(member, cartItemIds);
        Long savedOrderId = orderRepository.save(draftOrder);
        cartItemIds.forEach(cartItemRepository::deleteById);
        return savedOrderId;
    }

    // TODO: 예외 정리하기
    public Order retrieveOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public List<Order> retrieveMemberOrders(Member member) {
        return orderRepository.findByMember(member);
    }
}
