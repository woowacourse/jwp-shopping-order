package com.woowahan.techcourse.coupon.ui.response;

import com.woowahan.techcourse.coupon.domain.Coupon;
import java.util.List;
import java.util.stream.Collectors;

public class CouponsResponse {

    private List<CouponResponse> coupons;

    private CouponsResponse() {
    }

    public CouponsResponse(List<Coupon> coupons) {
        this.coupons = coupons.stream()
                .map(CouponResponse::new)
                .collect(Collectors.toList());
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
