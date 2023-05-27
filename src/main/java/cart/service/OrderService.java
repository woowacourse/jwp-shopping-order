package cart.service;

import cart.domain.cart.Item;
import cart.domain.cart.Order;
import cart.domain.coupon.Coupon;
import cart.dto.ItemIdDto;
import cart.dto.OrderResponse;
import cart.dto.OrderSaveRequest;
import cart.exception.CouponNotFoundException;
import cart.exception.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
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
    private final CouponRepository couponRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final CartItemRepository cartItemRepository,
            final CouponRepository couponRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    public Long save(final OrderSaveRequest orderSaveRequest, final Long memberId) {
        final List<Long> itemIds = orderSaveRequest.getOrderItems().stream()
                .map(ItemIdDto::getId)
                .collect(Collectors.toList());
        final List<Item> items = cartItemRepository.findAllByIds(itemIds, memberId);
        if (Objects.nonNull(orderSaveRequest.getCouponId())) {
            final Coupon coupon = couponRepository.findByIdAndMemberId(orderSaveRequest.getCouponId(), memberId)
                    .orElseThrow(CouponNotFoundException::new);
            final Order order = orderRepository.save(new Order(coupon, memberId, items));
            return order.getId();
        }
        final Order order = orderRepository.save(new Order(Coupon.EMPTY, memberId, items));
        return order.getId();
    }

    public List<OrderResponse> findAll(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(final Long id, final Long memberId) {
        return orderRepository.findById(id, memberId)
                .map(OrderResponse::from)
                .orElseThrow(OrderNotFoundException::new);
    }
}
