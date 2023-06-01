package cart.coupon.presentation.dto;

import cart.coupon.domain.Coupon;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AllCouponQueryResponse {

    private final List<CouponResponse> allCoupons;
    private final List<CouponResponse> specificCoupons;

    public AllCouponQueryResponse(List<CouponResponse> allCoupons, List<CouponResponse> specificCoupons) {
        this.allCoupons = allCoupons;
        this.specificCoupons = specificCoupons;
    }

    public static AllCouponQueryResponse from(List<Coupon> coupons) {
        Map<Class<?>, List<CouponResponse>> mapByType = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.groupingBy(CouponResponse::getClass));
        return new AllCouponQueryResponse(
                mapByType.get(GeneralCouponResponse.class),
                mapByType.get(SpecificCouponResponse.class));
    }

    public List<CouponResponse> getAllCoupons() {
        return allCoupons;
    }

    public List<CouponResponse> getSpecificCoupons() {
        return specificCoupons;
    }
}
