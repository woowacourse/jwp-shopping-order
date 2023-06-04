package cart.domain;

import cart.exception.NoExpectedException;

import java.util.Arrays;
import java.util.Objects;

public enum ShippingFee {
    BASIC(3000),
    NONE(0);

    public static final int FREE_DELIVERY_CRITERIA = 30000;
    private final Integer charge;

    ShippingFee(final Integer charge) {
        this.charge = charge;
    }
    
    public static ShippingFee from(final Integer price) {
        if (price > FREE_DELIVERY_CRITERIA) {
            return NONE;
        }
        return BASIC;
    }

    public static ShippingFee findByCharge(final Integer charge) {
        return Arrays.stream(values())
                .filter(shippingFee -> Objects.equals(shippingFee.getCharge(), charge))
                .findFirst()
                .orElseThrow(() -> new NoExpectedException("해당 배송비 정책이 존재하지 않습니다."));
    }

    public Integer getCharge() {
        return charge;
    }
}
