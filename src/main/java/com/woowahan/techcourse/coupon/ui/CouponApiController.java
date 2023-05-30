package com.woowahan.techcourse.coupon.ui;

import com.woowahan.techcourse.coupon.application.CouponQueryService;
import com.woowahan.techcourse.coupon.ui.response.CouponsResponse;
import com.woowahan.techcourse.member.ui.resolver.Authentication;
import com.woowahan.techcourse.member.ui.resolver.MemberId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CouponApiController {

    private final CouponQueryService couponQueryService;

    public CouponApiController(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @GetMapping("/coupons")
    public CouponsResponse findAll() {
        return new CouponsResponse(couponQueryService.findAllCoupons());
    }

    @GetMapping("/members/coupons")
    public CouponsResponse findAllByMemberId(@Authentication MemberId memberId) {
        return new CouponsResponse(couponQueryService.findAllCouponsByMemberId(memberId.getId()));
    }
}
