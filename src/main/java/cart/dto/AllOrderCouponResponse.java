package cart.dto;

import java.util.List;
import java.util.Objects;

public class AllOrderCouponResponse {
    private List<OrderCouponResponse> coupons;

    public AllOrderCouponResponse() {
    }

    public AllOrderCouponResponse(final List<OrderCouponResponse> coupons) {
        this.coupons = coupons;
    }

    public List<OrderCouponResponse> getCoupons() {
        return coupons;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AllOrderCouponResponse that = (AllOrderCouponResponse) o;
        return Objects.equals(coupons, that.coupons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coupons);
    }

    @Override
    public String toString() {
        return "AllOrderCouponResponse{" +
                "coupons=" + coupons +
                '}';
    }
}
