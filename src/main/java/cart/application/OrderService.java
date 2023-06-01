package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.OrderProductRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.request.OrderRequest;
import cart.exception.OrderException;
import cart.domain.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(CartItemRepository cartItemRepository, MemberCouponRepository memberCouponRepository, OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public Long save(Member member, OrderRequest orderRequest) {
        if (orderRequest.getSelectCartIds().isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }

        List<CartItem> cartItems = cartItemRepository.findAllByIds(member, orderRequest.getSelectCartIds());

        Order order = new Order(
                member, cartItems,
                memberCouponRepository.findAvailableCouponByMember(member, orderRequest.getCouponId()));

        Long orderSavedId = orderRepository.saveOrder(order);
        cartItemRepository.deleteByMemberCartItemIds(member.getId(), cartItems);
        orderProductRepository.saveOrderProductsByOrderId(orderSavedId,order);
        memberCouponRepository.changeUserUsedCouponAvailability(order.getCoupon());
        return orderSavedId;
    }
}
