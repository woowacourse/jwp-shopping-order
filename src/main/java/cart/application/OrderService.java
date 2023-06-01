package cart.application;

import static cart.exception.OrderException.IllegalPointUse;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.ui.controller.dto.request.OrderRequest;
import cart.ui.controller.dto.response.CartItemResponse;
import cart.ui.controller.dto.response.OrderResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            MemberRepository memberRepository,
            CartItemRepository cartItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<OrderResponse> getOrders(Member member) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderDetail(Long id, Member member) {
        Order order = orderRepository.findById(id);
        order.checkOwner(member);
        return OrderResponse.from(order);
    }

    @Transactional
    public List<CartItemResponse> processOrder(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItems = cartItemRepository.findAllInIds(orderRequest.getCartItemIds());
        for (CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        Order order = new Order(member, orderInCart(cartItems), orderRequest.getPoint());
        if (order.getTotalPrice() < orderRequest.getPoint()) {
            throw new IllegalPointUse(order.getTotalPrice(), orderRequest.getPoint());
        }
        member.usePoint(orderRequest.getPoint());
        member.addPoint(order.getSavedPoint());

        orderRepository.save(order);
        memberRepository.update(member);
        cartItemRepository.deleteAll(cartItems);
        return cartItemRepository.findByMemberId(member.getId())
                .stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    private List<OrderProduct> orderInCart(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(OrderProduct::from)
                .collect(Collectors.toList());
    }
}
