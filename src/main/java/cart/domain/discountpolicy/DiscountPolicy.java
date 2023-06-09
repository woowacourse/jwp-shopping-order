package cart.domain.discountpolicy;

import java.util.List;

public interface DiscountPolicy {

    int discount(int totalPrice, List<CouponType> coupons, int point);

}
