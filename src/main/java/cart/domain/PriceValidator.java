package cart.domain;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.ProductDao;
import cart.exception.OrdersPriceNotMatchException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceValidator {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;

    public PriceValidator(ProductRepository productRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    public void validateOrders(final List<Long> cartIds, final int orderPrice, final List<Long> couponIds){
        final int originalPrice = cartItemRepository.findProductQuantityByCartItemIds(cartIds).entrySet().stream()
                .map(entry -> productRepository.getPriceById(entry.getKey())*entry.getValue())
                .reduce(0,Integer::sum);
        int discountPrice = originalPrice;
        List<Coupon> coupons = couponRepository.findCouponsById(couponIds);
        for(Coupon coupon: coupons){
            discountPrice = (int) ((1-coupon.getDiscountRate()) * discountPrice) + coupon.getDiscountAmount();
            validateLimit(orderPrice,coupon.getMinimumPrice());
        }
        validateOrderPrice(discountPrice,orderPrice);
    }
    private void validateLimit(final int price, final int limit){
        if(price<limit){
            throw new OrdersPriceNotMatchException( limit + "이상으로 구매하셔야 합니다. ( 현재금액 : " + price +")");
        }
    }
        private void validateOrderPrice(final int price, final int orderPrice){
        if(price!=orderPrice){
            throw new OrdersPriceNotMatchException("요청 금액("+orderPrice +")과 실제 금액("+price+")이 다릅니다.");
        }
    }
}
