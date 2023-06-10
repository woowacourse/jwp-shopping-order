package cart.coupon.presentation;

import cart.coupon.application.CouponQueryService;
import cart.coupon.presentation.dto.AllCouponQueryResponse;
import cart.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons")
@RestController
public class CouponApiController {

    private final CouponQueryService couponQueryService;

    public CouponApiController(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @GetMapping
    ResponseEntity<AllCouponQueryResponse> findAllByMember(
            Member member
    ) {
        return ResponseEntity.ok(
                couponQueryService.findAllByMemberId(member.getId()));
    }
}
