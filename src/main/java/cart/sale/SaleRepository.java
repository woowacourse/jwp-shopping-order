package cart.sale;

import cart.discountpolicy.application.DiscountPolicyRepository;
import cart.discountpolicy.discountcondition.DiscountTarget;
import org.springframework.stereotype.Repository;

import java.util.*;

public interface SaleRepository {
    Long save(String name, Long discountPolicyId);

    Sale findById(Long id);

    List<Sale> findAllExcludingTarget(List<DiscountTarget> discountTargets);

    List<Sale> findAllApplyingToTotalPrice();
}
