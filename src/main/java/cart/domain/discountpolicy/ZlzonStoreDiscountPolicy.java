package cart.domain.discountpolicy;

import cart.exception.OverFullPointException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZlzonStoreDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int totalPrice, List<CouponType> coupons, int point) {
        int currentPrice = totalPrice;
        for (CouponType coupon : coupons) {
            currentPrice -= coupon.applyDiscount(currentPrice);
        }

        if (point > currentPrice) {
            throw new OverFullPointException("사용하려는 포인트가 결제 예상 금액보다 큽니다.");
        }

        currentPrice -= point;
        return currentPrice;
    }

}
