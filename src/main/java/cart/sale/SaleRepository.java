package cart.sale;

import cart.discountpolicy.discountcondition.DiscountTarget;

import java.util.List;

public interface SaleRepository {
    Long save(String name, Long discountPolicyId);

    Sale findById(Long id);

    List<Sale> findAllExcludingTarget(List<DiscountTarget> discountTargets);

    List<Sale> findAllApplyingToTotalPrice();
}
