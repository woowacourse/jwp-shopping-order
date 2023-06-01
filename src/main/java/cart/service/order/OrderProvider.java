package cart.service.order;

import cart.controller.dto.OrderItemResponse;
import cart.controller.dto.OrderResponse;
import cart.controller.dto.ProductResponse;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderProvider {

    private final OrderRepository orderRepository;

    public OrderProvider(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrderByMember(final Member member) {
        final List<Order> orders = orderRepository.findOrderByMemberId(member.getId());
        return orders.stream()
                .map(OrderProvider::createOrderResponse)
                .collect(toList());
    }

    private static OrderResponse createOrderResponse(final Order order) {
        final List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(OrderProvider::createOrderItem)
                .collect(toList());

        return new OrderResponse(
                order.getId(),
                orderItemResponses,
                Date.from(order.getDate().atZone(ZoneId.systemDefault()).toInstant()),
                order.getPrice()
        );
    }

    private static OrderItemResponse createOrderItem(final OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getQuantity(), ProductResponse.from(orderItem));
    }
}
