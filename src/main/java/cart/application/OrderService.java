package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.dto.response.OrderHistoryResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(
            final OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    public List<OrderHistoryResponse> getOrdersOf(final Member member) {
        final List<Order> orders = orderRepository.findOrdersOfMember(member);

        final List<OrderHistoryResponse> orderHistoryResponses = new ArrayList<>();

        for (final Order order : orders) {
            final Long orderId = order.getId();
            final int totalPrice = order.calculateTotalPrice();
            final int totalAmount = order.countTotalAmount();
            final List<String> productNames = order.getProductNames();
            final OrderHistoryResponse response = OrderHistoryResponse.from(orderId, totalPrice, totalAmount, productNames);
            orderHistoryResponses.add(response);
        }

        return orderHistoryResponses;
    }
}
