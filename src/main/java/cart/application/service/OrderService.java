package cart.application.service;

import cart.application.dto.order.OrderCartItemsRequest;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberCouponRepository;
import cart.application.repository.OrderRepository;
import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.exception.notfound.MemberCouponNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemRepository cartItemRepository,
            final MemberCouponRepository memberCouponRepository,
            final OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.orderRepository = orderRepository;
    }

    public long orderCartItems(final Member member, final OrderCartItemsRequest request) {
        CartItems cartItems = cartItemRepository.findByIds(request.getCartItemIds());
        cartItems.checkOwner(member);

        MemberCoupon memberCoupon = memberCouponRepository.findById(request.getCouponId())
                .orElseThrow(MemberCouponNotFoundException::new);
        memberCoupon.checkOwner(member);

        Order order = Order.of(member, cartItems, memberCoupon);

        cartItemRepository.deleteAll(cartItems);
        memberCouponRepository.use(memberCoupon);
        return orderRepository.order(order);
    }
}
