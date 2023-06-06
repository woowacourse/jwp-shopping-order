package cart.application;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.ui.controller.dto.request.OrderRequest;
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

    public List<OrderResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        return OrderResponse.listOf(orders);
    }

    public OrderResponse findById(Long id, Member member) {
        Order order = orderRepository.findById(id);
        order.checkOwner(member);
        return OrderResponse.from(order);
    }

    @Transactional
    public Long processOrder(Member member, OrderRequest orderRequest) {
        CartItems cartItems = cartItemRepository.findAllInIds(orderRequest.getCartItemIds());
        cartItems.checkOwner(member);
        Order order = new Order(member, orderInCart(cartItems), orderRequest.getPoint());
        order.checkPointAvailable(orderRequest.getPoint());
        member.usePoint(orderRequest.getPoint());
        member.addPoint(order.getSavedPoint());

        memberRepository.update(member);
        cartItemRepository.deleteAll(cartItems);
        return orderRepository.save(order);
    }

    private List<OrderProduct> orderInCart(CartItems cartItems) {
        return cartItems.getCartItems()
                .stream()
                .map(OrderProduct::from)
                .collect(Collectors.toList());
    }
}
