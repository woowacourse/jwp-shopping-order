package cart.repository;

import cart.dao.DefaultDeliveryPolicyDao;
import cart.domain.DeliveryPolicy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliveryPolicyDbRepository implements DeliveryPolicyRepository {

    private final DefaultDeliveryPolicyDao defaultDeliveryPolicyDao;

    public DeliveryPolicyDbRepository(final DefaultDeliveryPolicyDao defaultDeliveryPolicyDao) {
        this.defaultDeliveryPolicyDao = defaultDeliveryPolicyDao;
    }

    @Override
    public List<DeliveryPolicy> findDefault() {
        return this.defaultDeliveryPolicyDao.findDefault();
    }
}
