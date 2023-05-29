package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.exception.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final MemberRepository memberRepository,
            final CartItemRepository cartItemRepository,
            final MemberCouponRepository memberCouponRepository
    ) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long save(final Long memberId, final OrderRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        final List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);

        final MemberCoupon usedCoupon = useCouponIfExist(memberId, request.getCouponId());
        final Order order = Order.createFromCartItems(cartItems, 3000L, usedCoupon, member);

        cartItemRepository.deleteAll(cartItems);
        return orderRepository.save(order).getId();
    }

    private MemberCoupon useCouponIfExist(final Long memberId, final Long couponId) {
        if (Objects.isNull(couponId)) {
            return new MemberCoupon.NullMemberCoupon();
        }
        final MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId)
                .orElseThrow(MemberCouponNotFoundException::new);
        final MemberCoupon usedCoupon = memberCoupon.use();
        return memberCouponRepository.save(usedCoupon);
    }

    public List<OrderResponse> findAll(final Long memberId) {
        final List<Order> orders = orderRepository.findByMemberId(memberId);

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(final Long memberId, final Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        order.checkOwner(member);

        return OrderResponse.from(order);
    }
}
