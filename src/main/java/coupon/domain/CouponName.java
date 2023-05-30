package coupon.domain;

import coupon.exception.CouponException;
import java.util.Objects;

public class CouponName {

    private static final int MAX_NAME_LENGTH = 255;
    private final String value;

    public CouponName(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value.length() > MAX_NAME_LENGTH) {
            throw new CouponException("쿠폰 이름은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CouponName that = (CouponName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
