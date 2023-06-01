package cart.repository;

import cart.dao.ShippingFeeDao;
import cart.dao.ShippingPolicyDao;
import cart.domain.ShippingPolicy;
import cart.exception.PolicyException.NoShippingDiscountThreshold;
import cart.exception.PolicyException.NoShippingFee;
import org.springframework.stereotype.Repository;

@Repository
public class ShippingPolicyRepository {

    private final ShippingFeeDao shippingFeeDao;
    private final ShippingPolicyDao shippingPolicyDao;

    public ShippingPolicyRepository(final ShippingFeeDao shippingFeeDao, final ShippingPolicyDao shippingPolicyDao) {
        this.shippingFeeDao = shippingFeeDao;
        this.shippingPolicyDao = shippingPolicyDao;
    }

    public ShippingPolicy findShippingPolicy() {
        long basicShippingFee = shippingFeeDao.selectShippingFee()
                .orElseThrow(NoShippingFee::new);
        long threshold = shippingPolicyDao.selectDiscountThreshold()
                .orElseThrow(NoShippingDiscountThreshold::new);
        return new ShippingPolicy(basicShippingFee, threshold);
    }
}
