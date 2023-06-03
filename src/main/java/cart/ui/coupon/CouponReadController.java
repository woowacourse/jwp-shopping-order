package cart.ui.coupon;

import cart.application.service.coupon.CouponReadService;
import cart.application.service.coupon.dto.CouponResultDto;
import cart.domain.member.Member;
import cart.ui.coupon.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponReadController {

    private final CouponReadService couponReadService;

    public CouponReadController(final CouponReadService couponReadService) {
        this.couponReadService = couponReadService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons(final Member memberAuth) {
        final List<CouponResultDto> couponResultDtos = couponReadService.findByMember(memberAuth);

        return ResponseEntity.ok(CouponResponse.from(couponResultDtos));
    }

}
