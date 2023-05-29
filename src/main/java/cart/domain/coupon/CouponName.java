package cart.domain.coupon;

import static cart.exception.ErrorCode.COUPON_NAME_LENGTH;

import cart.exception.BadRequestException;
import java.util.Objects;

public class CouponName {

    private static final int NAME_MIN_LENGTH = 1, NAME_MAX_LENGTH = 50;

    private final String name;

    private CouponName(final String name) {
        this.name = name;
    }

    public static CouponName create(final String name) {
        validateLength(name);
        return new CouponName(name);
    }

    private static void validateLength(final String name) {
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new BadRequestException(COUPON_NAME_LENGTH);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CouponName that = (CouponName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
