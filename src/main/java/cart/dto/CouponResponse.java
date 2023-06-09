package cart.dto;

import cart.domain.Coupon;
import cart.domain.CouponType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        final CouponType couponType = coupon.getCouponType();
        return new CouponResponse(coupon.getId(), couponType.getName(), couponType.getDiscountAmount().getValue());
    }

    public static List<CouponResponse> from(final List<Coupon> coupons) {
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CouponResponse that = (CouponResponse) o;
        return id == that.id && priceDiscount == that.priceDiscount && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, priceDiscount);
    }
}
