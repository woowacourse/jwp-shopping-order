package cart.repository;

import cart.dao.OrderCouponDao;
import cart.domain.Order;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.OrderCouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCouponRepositoryImpl implements OrderCouponRepository {
    private final OrderCouponDao orderCouponDao;

    public OrderCouponRepositoryImpl(OrderCouponDao orderCouponDao) {
        this.orderCouponDao = orderCouponDao;
    }

    @Override
    public void save(Long orderId, Order order) {
        if (DiscountType.EMPTY_DISCOUNT.getTypeName().equals(order.getCoupon().getCouponTypes().getCouponTypeName())) {
            return;
        }
        orderCouponDao.save(orderId, order.getCoupon().getId());
    }

    @Override
    public Long deleteByOrderId(Long orderId) {
        if (orderCouponDao.checkByOrderId(orderId)) {
            Long memberCouponId = orderCouponDao.findIdByOrderId(orderId);
            orderCouponDao.deleteByOrderId(orderId);
            return memberCouponId;
        }
        return null;
    }
}
