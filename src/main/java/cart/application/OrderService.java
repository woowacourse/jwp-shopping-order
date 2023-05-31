package cart.application;

import cart.domain.CartItems;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.coupon.Coupon;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CouponRepository couponRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long order(final OrderRequest request) {
        final List<Long> cartItemIds = request.getId();
        final CartItems cartItems = cartItemRepository.findAllByCartItemIds(cartItemIds);
        if (cartItems.isNotSameSize(cartItemIds.size())) {
            throw new NoSuchElementException("존재하지 않는 상품이 포함되어 있습니다.");
        }

        Coupon coupon = checkCoupon(request, cartItems.getMemberId());

        cartItemRepository.deleteAllByIds(cartItemIds);

        final List<OrderItem> orderItems = cartItems.getCartItems().stream()
                .map(it -> new OrderItem(it.getProduct(), it.getQuantity()))
                .collect(toList());

        final Order order = new Order(orderItems, cartItems.getMember(), coupon, request.getPrice());
        return orderRepository.save(order);
    }

    private Coupon checkCoupon(final OrderRequest request, final Long memberId) {
        Coupon coupon = Coupon.empty();
        if (hasCoupon(request.getCouponId())) {
            coupon = findCouponAndUse(request.getCouponId(), memberId);
        }

        return coupon;
    }

    private boolean hasCoupon(final Long couponId) {
        return !ObjectUtils.isEmpty(couponId);
    }

    private Coupon findCouponAndUse(final Long couponId, final Long memberId) {
        final Coupon coupon = couponRepository.findCouponById(couponId);

        couponRepository.changeStatus(couponId, memberId);

        return coupon;
    }
}
