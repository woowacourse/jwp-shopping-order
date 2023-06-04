package cart.application;

import cart.application.request.OrderRequest;
import cart.application.response.OrderWithOutTotalPriceResponse;
import cart.application.response.OrderWithTotalPriceResponse;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderHistory;
import cart.domain.vo.Money;
import cart.event.MemberUpdateEvent;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cart.domain.pay.PointPolicy.DEFAULT_POINT_POLICY;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository, ApplicationEventPublisher publisher) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Long createOrder(Member member, OrderRequest request) {
        Money usePoint = Money.from(request.getPoint());
        Money deliveryFee = Money.from(request.getDeliveryFee());
        CartItems findCartItems = cartItemRepository.findByCartItemIds(request.getCartIds());

        Order order = Order.of(member, findCartItems, deliveryFee, usePoint, DEFAULT_POINT_POLICY);
        publisher.publishEvent(MemberUpdateEvent.from(member));

        cartItemRepository.deleteByCartItemIds(request.getCartIds());

        return orderRepository.saveOrder(order);
    }

    public OrderWithTotalPriceResponse findByOrderId(Member member, Long orderId) {
        OrderHistory orderHistory = orderRepository.findByOrderId(orderId);

        if (orderHistory.isNotOwner(member)) {
            throw new OrderException.NotOwner();
        }

        return OrderWithTotalPriceResponse.from(orderHistory);
    }

    public List<OrderWithOutTotalPriceResponse> findAllByMemberId(Long memberId) {
        return orderRepository.findOrdersByMemberId(memberId)
                .stream()
                .map(OrderWithOutTotalPriceResponse::from)
                .collect(Collectors.toList());
    }
}
