package cart.application;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Payment;
import cart.domain.PaymentGenerator;
import cart.domain.Price;
import cart.dto.order.OrderSimpleResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentGenerator paymentGenerator;

    public OrderService(final OrderRepository orderRepository, PaymentGenerator paymentGenerator) {
        this.orderRepository = orderRepository;
        this.paymentGenerator = paymentGenerator;
    }

    public Long createOrder(final Member member, final OrderRequest request) {
        CartItems cartItems = orderRepository.findCartItemsByMemberId(member);
        Payment payment = getPayment(request, cartItems);
        OrderItems orderItems = createOrderItems(cartItems);
        return orderRepository.createOrder(new Order(payment, orderItems, member), cartItems);
    }

    private Payment getPayment(final OrderRequest request, final CartItems cartItems) {
        Payment payment = paymentGenerator.generate(calculateTotalPrice(cartItems, request));
        validateFinalPrice(request, payment);
        return payment;
    }

    private void validateFinalPrice(final OrderRequest request, final Payment payment) {
        if (!payment.equalsFinalPrice(new Price(request.getFinalPrice()))) {
            throw new IllegalArgumentException("계산된 가격이 잘못되었습니다.");
        }
    }

    private Price calculateTotalPrice(final CartItems cartItems, final OrderRequest request) {
        CartItems subCartItems = cartItems.getSubCartItems(request.getCartItemIds());
        return subCartItems.calculateTotalPrice();
    }

    private OrderItems createOrderItems(final CartItems cartItems) {
        return new OrderItems(cartItems.getItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList()));
    }

    public OrderResponse findOrder(final Long orderId, final Member member) {
        Order order = orderRepository.findOrder(orderId, member);
        return OrderResponse.from(order);
    }

    public List<OrderSimpleResponse> findAllByMember(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);
        return orders.stream()
                .map(OrderSimpleResponse::from)
                .collect(Collectors.toList());
    }
}
