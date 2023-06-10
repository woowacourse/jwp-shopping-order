package cart.application;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.order.OrderProductsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
        Order order = Order.orderProductsAndUpdatePayment(member, cartItems, orderProductsRequest.getPoint(), orderProductsRequest.getDeliveryFee());
        member.pay(order.getPayment());
        // 장바구니에서 삭제
        cartItems.forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        // 주문 목록에 저장
        long orderId = orderRepository.createOrder(order);
        // 포인트, 결제 내용 정보 저장
        memberRepository.updatePoint(member.getId(), member.getAvailablePointValue());
        memberRepository.updateMoney(member.getId(), member.getAvailableMoneyValue());
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
