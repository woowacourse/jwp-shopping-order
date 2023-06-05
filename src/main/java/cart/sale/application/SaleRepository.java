package cart.sale.application;

import cart.sale.Sale;

import java.util.List;

public interface SaleRepository {
    Long save(String name, Long discountPolicyId);

    Sale findById(Long id);

    List<Sale> findAllSales();
}
