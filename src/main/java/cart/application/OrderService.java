package cart.application;

import cart.application.request.OrderRequest;
import cart.application.response.OrderWithOutTotalPriceResponse;
import cart.application.response.OrderWithTotalPriceResponse;
import cart.application.response.OrdersResponse;
import cart.domain.cartitem.CartItem;
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

import static cart.domain.pay.PayPoint.DEFAULT_POINT_POLICY;

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
        CartItems findCartItems = cartItemRepository.findByCartItemIds(request.getCartItemIds());
        findCartItems.checkOwner(member);

        Order order = Order.of(member, findCartItems, usePoint, DEFAULT_POINT_POLICY);
        publisher.publishEvent(MemberUpdateEvent.from(member));

        cartItemRepository.deleteByCartItemIds(collectCartItemIds(findCartItems));

        return orderRepository.saveOrder(order);
    }

    private List<Long> collectCartItemIds(CartItems cartItems) {
        return cartItems.getCartItems()
                .stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public OrderWithTotalPriceResponse findByOrderId(Member member, Long orderId) {
        OrderHistory orderHistory = orderRepository.findByOrderId(orderId);

        if (member.isNotSame(orderHistory.getMember())) {
            throw new OrderException.NotOwner();
        }

        return OrderWithTotalPriceResponse.from(orderRepository.findByOrderId(orderId));
    }

    public OrdersResponse findAllByMemberId(Long memberId) {
        List<OrderWithOutTotalPriceResponse> orderResponses = orderRepository.findOrdersByMemberId(memberId)
                .stream()
                .map(OrderWithOutTotalPriceResponse::from)
                .collect(Collectors.toList());

        return OrdersResponse.from(orderResponses);
    }
}
