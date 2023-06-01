package cart.service.order;

import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import cart.exception.AlreadyUsedCouponException;
import cart.service.dto.OrderRequest;
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
        final CartItems cartItems = cartItemRepository.findAllByCartItemIds(request.getCartItemIds());
        if (cartItems.isNotSameSize(request.getCartItemIds().size())) {
            throw new NoSuchElementException("존재하지 않는 상품이 포함되어 있습니다.");
        }

        Coupon coupon = checkCoupon(request, cartItems.getMemberId());

        cartItemRepository.deleteAllByIds(request.getCartItemIds());

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

        if (coupon.isUsed()) {
            throw new AlreadyUsedCouponException();
        }

        couponRepository.changeStatusTo(couponId, memberId, Boolean.TRUE);

        return coupon;
    }

}
