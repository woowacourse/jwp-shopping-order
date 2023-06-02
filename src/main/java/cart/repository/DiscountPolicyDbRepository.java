package cart.repository;

import cart.dao.DefaultDiscountPolicyDao;
import cart.domain.DiscountPolicy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiscountPolicyDbRepository implements DiscountPolicyRepository {
    private final DefaultDiscountPolicyDao defaultDiscountPolicyDao;

    public DiscountPolicyDbRepository(final DefaultDiscountPolicyDao defaultDiscountPolicyDao) {
        this.defaultDiscountPolicyDao = defaultDiscountPolicyDao;
    }

    @Override
    public List<DiscountPolicy> findDefault() {
        return this.defaultDiscountPolicyDao.findDefault();
    }
}
