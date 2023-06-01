package cart.domain.coupon;

import cart.exception.CouponTypeNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CouponType {

    RATE_DISCOUNT,
    CONSTANT_DISCOUNT;

    private static final Map<String, CouponType> COUPON_TYPE_MAP
            = Arrays.stream(CouponType.values())
            .collect(Collectors.toMap(CouponType::toString, Function.identity()));

    public static CouponType from(final String name) {
        if (!COUPON_TYPE_MAP.containsKey(name)) {
            throw new CouponTypeNotFoundException();
        }
        return COUPON_TYPE_MAP.get(name);
    }
}
