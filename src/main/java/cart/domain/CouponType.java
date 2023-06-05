package cart.domain;

import java.util.Arrays;

public enum CouponType {
    FIGURE("figure"),
    PERCENT("percent");

    private final String text;


    CouponType(String text) {
        this.text = text;
    }

    public static CouponType from(String text) {
        return Arrays.stream(CouponType.values())
                .filter(couponType -> couponType.text.equals(text))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
