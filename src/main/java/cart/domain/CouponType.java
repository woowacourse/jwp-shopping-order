package cart.domain;

import java.util.Arrays;

public enum CouponType {
    PERCENTAGE("percentage"),
    DEDUCTION("deduction");
    private final String value;

    CouponType(final String value){
        this.value = value;
    }

    public static CouponType mappingType(final String type){
        return Arrays.stream(values())
                .filter(couponType -> couponType.value.equals(type))
                .findAny()
                .orElseThrow();
    }
}
