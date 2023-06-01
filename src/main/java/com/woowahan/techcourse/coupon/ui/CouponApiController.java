package com.woowahan.techcourse.coupon.ui;

import com.woowahan.techcourse.coupon.application.CouponCommandService;
import com.woowahan.techcourse.coupon.application.CouponQueryService;
import com.woowahan.techcourse.coupon.ui.request.AddMemberCouponRequest;
import com.woowahan.techcourse.coupon.ui.response.CouponsResponse;
import com.woowahan.techcourse.member.ui.resolver.Authentication;
import com.woowahan.techcourse.member.ui.resolver.MemberId;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CouponApiController {

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;

    public CouponApiController(CouponQueryService couponQueryService, CouponCommandService couponCommandService) {
        this.couponQueryService = couponQueryService;
        this.couponCommandService = couponCommandService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponsResponse> findAll() {
        CouponsResponse response = new CouponsResponse(couponQueryService.findAllCoupons());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/coupons")
    public ResponseEntity<CouponsResponse> findAllByMemberId(@Authentication MemberId memberId) {
        CouponsResponse response = new CouponsResponse(couponQueryService.findAllCouponsByMemberId(memberId.getId()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members/coupon")
    public ResponseEntity<Void> addCoupon(@Authentication MemberId memberId,
            @RequestBody @Valid AddMemberCouponRequest request) {
        couponCommandService.addCoupon(request.getCouponId(), memberId.getId());
        return ResponseEntity.ok().build();
    }
}
