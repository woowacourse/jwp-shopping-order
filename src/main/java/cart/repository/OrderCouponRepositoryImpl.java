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
    public void saveOrderCoupon(Long orderId, Order order) {
        if (DiscountType.NONE.getTypeName().equals(order.getCoupon().getCouponTypes().getCouponTypeName())) {
            return;
        }
        orderCouponDao.saveOrderCoupon(orderId, order.getCoupon().getId());
    }
}
