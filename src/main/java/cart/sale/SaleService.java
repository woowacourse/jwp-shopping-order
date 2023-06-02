package cart.sale;

import cart.cart.Cart;
import cart.discountpolicy.application.DiscountPolicyService;
import org.springframework.stereotype.Service;

@Service
public class SaleService {
    private final DiscountPolicyService discountPolicyService;
    private final SaleRepository saleRepository;

    public SaleService(DiscountPolicyService discountPolicyService, SaleRepository saleRepository) {
        this.discountPolicyService = discountPolicyService;
        this.saleRepository = saleRepository;
    }

    public void applySale(Cart cart) {
        for (Sale sale : saleRepository.findAll()) {
            discountPolicyService.applyPolicy(sale.getDiscountPolicyId(), cart);
        }
    }
}
