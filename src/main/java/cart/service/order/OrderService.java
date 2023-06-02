package cart.service.order;

import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.Price;
import cart.exception.AlreadyUsedCouponException;
import cart.service.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.NoSuchElementException;

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

        Coupon coupon = findCoupon(request.getCouponId());

        cartItemRepository.deleteAllByIds(request.getCartItemIds());

        final Price price = Price.from(request.getPrice());
        final Order order = Order.create(cartItems, coupon, price);
        return orderRepository.save(order);
    }

    private Coupon findCoupon(final Long couponId) {
        if (ObjectUtils.isEmpty(couponId)) {
            return Coupon.empty();
        }

        return findCouponAndUse(couponId);
    }

    private Coupon findCouponAndUse(final Long couponId) {
        final Coupon coupon = couponRepository.findCouponById(couponId);

        if (coupon.isUsed()) {
            throw new AlreadyUsedCouponException();
        }

        couponRepository.changeStatusTo(couponId, Boolean.TRUE);
        return coupon;
    }

}
