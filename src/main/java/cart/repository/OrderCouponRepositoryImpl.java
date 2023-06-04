package cart.repository;

import cart.dao.OrderCouponDao;
import cart.domain.repository.OrderCouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCouponRepositoryImpl implements OrderCouponRepository {
    private final OrderCouponDao orderCouponDao;

    public OrderCouponRepositoryImpl(OrderCouponDao orderCouponDao) {
        this.orderCouponDao = orderCouponDao;
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
