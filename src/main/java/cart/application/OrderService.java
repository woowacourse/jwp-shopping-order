package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.response.OrderHistoryResponse;
import cart.dto.response.OrderProductResponse;
import cart.exception.AccessNonAvailableMemberException;
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

    public List<OrderHistoryResponse> getOrderHistoriesOf(final Member member) {
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

    public List<OrderProductResponse> getOrderProductsOf(final Member member, final Long orderHistoryId) {
        if (!orderRepository.isAccessibleToOrder(member, orderHistoryId)) {
            throw new AccessNonAvailableMemberException("해당 주문 건의 사용자만 주문 건에 대한 상세조회에 접근할 수 있습니다!");
        }

        final Order order = orderRepository.findOrderProductOf(orderHistoryId);

        List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for (final Product product : order.getOrderedProducts().keySet()) {
            final int quantity = order.getOrderedProducts().get(product);
            final OrderProductResponse response = new OrderProductResponse(
                    product.getName(),
                    product.getImageUrl(),
                    quantity,
                    product.getPrice()
            );
            orderProductResponses.add(response);
        }

        return orderProductResponses;
    }
}
