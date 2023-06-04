package cart.dto;

import java.util.List;
import java.util.Objects;

public class AllCouponResponse {
    private List<CouponResponse> coupons;

    public AllCouponResponse() {
    }

    public AllCouponResponse(final List<CouponResponse> coupons) {
        this.coupons = coupons;
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
