package cart.application;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderProductsRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public long orderProducts(Member member, OrderProductsRequest orderProductsRequest) {
        List<CartItem> cartItems = cartItemRepository.findCartItemsByIds(orderProductsRequest.getCartIds());
        Order order = Order.of(member, cartItems, orderProductsRequest.getPoint());
        // 장바구니에서 삭제
        cartItems.forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        // 주문 목록에 저장
        long orderId = orderRepository.createOrder(order);
        // 포인트 정보 저장
        memberRepository.updatePoint(member.getId(), member.getPoint());
        return orderId;
    }

    // 사용자별 주문 내역
    public List<Order> getOrderByMember(Member member) {
        return orderRepository.findOrderProductsByMemberId(member);
    }

    // 주문 상세
    public Order getOrderDetailById(Member member, long orderId) {
        return orderRepository.findOrderById(member, orderId);
    }
}
