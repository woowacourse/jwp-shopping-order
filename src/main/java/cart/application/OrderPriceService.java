package cart.application;

import cart.dao.CouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.order.DeliveryFee;
import cart.domain.order.OrderPrice;
import cart.domain.order.OrderProduct;
import cart.dto.request.OrderRequestDto;
import cart.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPriceService {

    private final CouponDao couponDao;

    public OrderPriceService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public OrderPrice calculatePriceOf(final Member member, final List<OrderProduct> orderProducts, final OrderRequestDto orderRequestDto) {
        if (orderRequestDto.getCouponId() == null) {
            return OrderPrice.of(orderProducts, new DeliveryFee(3000));
        }
        final Coupon coupon = findCoupon(member, orderRequestDto);
        return coupon.apply(OrderPrice.of(orderProducts, new DeliveryFee(3000)));
    }

    private Coupon findCoupon(final Member member, final OrderRequestDto orderRequestDto) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        return couponById.stream()
                .filter(coupon -> coupon.getId() == orderRequestDto.getCouponId())
                .findAny()
                .orElseThrow(() -> {
                    throw new CouponNotFoundException("해당 쿠폰을 유저가 가지고 있지 않습니다,");
                });
    }
}
