package cart.repository;

import cart.dao.DefaultDiscountPolicyDao;
import cart.domain.DiscountPolicy;
import cart.exception.DiscountPolicyException;
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
        try {
            return this.defaultDiscountPolicyDao.findDefault();
        } catch (final IllegalArgumentException e) {
            throw new DiscountPolicyException.InvalidPolicyException(e.getMessage());
        }
    }
}
