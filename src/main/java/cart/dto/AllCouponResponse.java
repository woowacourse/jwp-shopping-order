package cart.dto;

import cart.domain.Coupon;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AllCouponResponse {
    private List<CouponResponse> coupons;

    public AllCouponResponse() {
    }

    public AllCouponResponse(final List<CouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static AllCouponResponse from(final List<Coupon> coupons) {
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new AllCouponResponse(couponResponses);
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AllCouponResponse that = (AllCouponResponse) o;
        return Objects.equals(coupons, that.coupons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coupons);
    }

    @Override
    public String toString() {
        return "AllCouponResponse{" +
                "coupons=" + coupons +
                '}';
    }
}
