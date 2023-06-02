package cart.sale;

import cart.cart.Cart;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountTarget;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void applySales(Cart cart, DiscountTarget... targetToExclude) {
        final var targetsToExclude = Arrays.stream(targetToExclude).collect(Collectors.toList());
        for (Sale sale : saleRepository.findAllExcludingTarget(targetsToExclude)) {
            sale.apply(cart);
        }
    }

    public void applySalesApplyingToTotalPrice(Cart cart) {
        for (Sale sale : saleRepository.findAllApplyingToTotalPrice()) {
            sale.apply(cart);
        }
    }
}
