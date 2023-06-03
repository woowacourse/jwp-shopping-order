package cart.dto;

import cart.domain.Coupon;
import cart.domain.CouponType;
import java.util.Objects;

public class CouponResponse {

    private final long id;
    private final String name;
    private final long priceDiscount;

    public CouponResponse(final long id, final String name, final long priceDiscount) {
        this.id = id;
        this.name = name;
        this.priceDiscount = priceDiscount;
    }

    public static CouponResponse from(final Coupon coupon) {
        if (Objects.nonNull(coupon)) {
            final CouponType couponType = coupon.getCouponType();
            return new CouponResponse(coupon.getId(), couponType.getName(), couponType.getDiscountAmount().getValue());
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPriceDiscount() {
        return priceDiscount;
    }
}
