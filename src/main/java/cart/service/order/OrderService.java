package cart.service.order;

import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.Price;
import cart.exception.network.AlreadyUsedCouponException;
import cart.exception.network.DifferentCartItemSizeException;
import cart.service.dto.OrderSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    public Long order(final OrderSaveDto saveDto) {
        final CartItems cartItems = cartItemRepository.findAllByCartItemIds(saveDto.getCartItemIds());
        if (cartItems.isNotSameSize(saveDto.getCartItemIds().size())) {
            throw new DifferentCartItemSizeException();
        }

        Coupon coupon = findCoupon(saveDto.getCouponId());

        cartItemRepository.deleteAllByIds(saveDto.getCartItemIds());

        final Price price = Price.from(saveDto.getPrice());
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
