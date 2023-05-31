package cart.service;

import cart.domain.cart.Item;
import cart.domain.cart.MemberCoupon;
import cart.domain.cart.Order;
import cart.dto.OrderResponse;
import cart.dto.OrderSaveRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderNotFoundException;
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

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository,
                        final MemberCouponRepository memberCouponRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long save(final OrderSaveRequest orderSaveRequest, final Long memberId) {
        final List<Item> items = cartItemRepository.findAllByIds(orderSaveRequest.getOrderItemIds(), memberId);
        if (Objects.nonNull(orderSaveRequest.getCouponId())) {
            final MemberCoupon memberCoupon = memberCouponRepository.findById(orderSaveRequest.getCouponId())
                    .orElseThrow(MemberCouponNotFoundException::new);
            final Order order = orderRepository.save(new Order(memberCoupon, memberId, items));
            return order.getId();
        }
        final Order order = orderRepository.save(new Order(MemberCoupon.empty(memberId), memberId, items));
        return order.getId();
    }

    public List<OrderResponse> findAll(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(final Long id, final Long memberId) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.checkOwner(memberId);

        return OrderResponse.from(order);
    }
}
