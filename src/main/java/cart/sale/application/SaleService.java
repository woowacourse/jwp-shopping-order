package cart.sale.application;

import cart.discountpolicy.DiscountPolicy;
import cart.sale.Sale;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<DiscountPolicy> findDiscountPoliciesFromSales() {
        final var sales = saleRepository.findAllSales();
        return sales.stream()
                .map(Sale::getDiscountPolicy)
                .collect(Collectors.toList());
    }
}
