package cart.domain;

import cart.domain.order.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderPriceBaseCouponGenerator implements OrderBaseCouponGenerator {

    public static final String COUPON_NAME = "5천원 할인 쿠폰";
    public static final int DISCOUNT_VALUE = 5000;
    public static final int MINIMUM_ORDER_AMOUNT = 0;
    public static final int EXPIRATION_PERIOD = 7;
    public static final int BONUS_COUPON_ISSUANCE_PRICE = 50000;

    @Override
    public Optional<Coupon> generate(Order order) {
        if (order.getFinalPrice() >= BONUS_COUPON_ISSUANCE_PRICE) {
            return Optional.of(new Coupon(null, COUPON_NAME, DISCOUNT_VALUE, MINIMUM_ORDER_AMOUNT, LocalDateTime.now().plusDays(EXPIRATION_PERIOD)));
        }
        return Optional.empty();
    }
}
