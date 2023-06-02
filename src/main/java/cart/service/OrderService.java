package cart.service;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberValidator;
import cart.domain.order.Order;
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

import java.math.BigDecimal;
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
        final List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);

        final MemberCoupon usedCoupon = useCouponIfExist(request.getCouponId());
        final Order order = Order.createFromCartItems(cartItems, new Money(BigDecimal.valueOf(3000L)), usedCoupon, memberId);

        cartItemRepository.deleteAll(cartItems);
        return orderRepository.save(order).getId();
    }

    private MemberCoupon useCouponIfExist(final Long couponId) {
        if (Objects.isNull(couponId)) {
            return new MemberCoupon.NullMemberCoupon();
        }
        final MemberCoupon memberCoupon = memberCouponRepository.findById(couponId)
                .orElseThrow(MemberCouponNotFoundException::new);
        return memberCoupon.use();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(final Long memberId) {
        final List<Order> orders = orderRepository.findByMemberId(memberId);

        return orders.stream()
                .map(order -> findById(memberId, order.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(final Long memberId, final Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        final MemberValidator memberValidator = new MemberValidator(member);
        order.validateMember(memberValidator);
        final Money discountedOrderPrice = order.discountOrderPrice();
        final Money discountedDeliveryFee = order.discountDeliveryFee();

        return OrderResponse.of(order, order.getOrderPrice(), discountedOrderPrice, discountedDeliveryFee);
    }
}
