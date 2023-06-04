package cart.repository;

import cart.dao.ShippingDiscountPolicyDao;
import cart.dao.ShippingFeeDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderPolicyRepository {

    private final ShippingDiscountPolicyDao shippingDiscountPolicyDao;
    private final ShippingFeeDao shippingFeeDao;

    public OrderPolicyRepository(ShippingDiscountPolicyDao shippingDiscountPolicyDao, ShippingFeeDao shippingFeeDao) {
        this.shippingDiscountPolicyDao = shippingDiscountPolicyDao;
        this.shippingFeeDao = shippingFeeDao;
    }

    public Long findThreshold() {
        return shippingDiscountPolicyDao.findShippingDiscountPolicy().get().getThreshold();
    }

    public Long findFee() {
        return shippingFeeDao.findFee().get().getFee();
    }
}
