package cart.domain;

import cart.dto.CouponResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdersValidator {
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;

    public OrdersValidator(CouponRepository couponRepository, CartItemRepository cartItemRepository) {
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
    }
    public boolean validate(final List<Long> cartIds, final Optional<Long> couponId){
        if(couponId.isEmpty()){
            return true;
        }
        final int price = calculateCartItemsOriginalPrice(cartIds);
        final int minimumPrice = couponRepository.findById(couponId.get()).getMinimumPrice();
        return price>=minimumPrice;
    }
    private int calculateCartItemsOriginalPrice(final List<Long> cartIds){
        return cartIds.stream()
                .map(id ->cartItemRepository.findTotalPriceByCartId(id))
                .reduce(0,Integer::sum);
    }
}
