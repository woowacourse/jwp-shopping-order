package cart.domain.coupon;


import cart.exception.CouponException;

import java.util.Arrays;

public enum DiscountType {
    percentDiscount("percentage", new PercentDiscount()),
    reductionDiscount("reduction", new PriceDiscount()),
    NONE("none", new EmptyDiscount());

    private final String typeName;
    private final CouponTypes type;

    DiscountType(String typeName, CouponTypes type) {
        this.typeName = typeName;
        this.type = type;
    }

    public static CouponTypes from(String input) {
        return Arrays.stream(DiscountType.values())
                .filter(it -> it.typeName.equalsIgnoreCase(input))
                .map(it -> it.type)
                .findAny()
                .orElseThrow(() -> new CouponException("percentage, reduction 타입의 쿠폰만 발급 가능합니다."));
    }

    public String getTypeName() {
        return typeName;
    }

    public CouponTypes getType() {
        return type;
    }
}
