package cart.service;

import cart.domain.cart.CartItem;
import cart.domain.cart.MemberCoupon;
import cart.domain.cart.Order;
import cart.dto.cart.OrderResponse;
import cart.dto.cart.OrderSaveRequest;
import cart.exception.cart.MemberCouponNotFoundException;
import cart.exception.cart.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final CartItemRepository cartItemRepository,
            final MemberCouponRepository memberCouponRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long save(final OrderSaveRequest request, final Long memberId) {
        final List<CartItem> items = cartItemRepository.findAllByIdsAndMemberId(request.getOrderItemIds(), memberId);

        if (Objects.nonNull(request.getCouponId())) {
            final MemberCoupon memberCoupon = memberCouponRepository.findById(request.getCouponId())
                    .orElseThrow(MemberCouponNotFoundException::new);
            final Order order = Order.of(memberCoupon, memberId, items);
            final Order saveOrder = orderRepository.save(order);
            return saveOrder.getId();
        }

        final Order order = Order.of(MemberCoupon.empty(memberId), memberId, items);
        final Order saveOrder = orderRepository.save(order);
        return saveOrder.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(final Long id, final Long memberId) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.checkOwner(memberId);

        return OrderResponse.from(order);
    }
}
