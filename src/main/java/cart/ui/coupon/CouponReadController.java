package cart.ui.coupon;

import cart.application.service.coupon.CouponReadService;
import cart.application.service.coupon.CouponResultDto;
import cart.ui.MemberAuth;
import cart.ui.coupon.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coupons")
public class CouponReadController {

    private final CouponReadService couponReadService;

    public CouponReadController(CouponReadService couponReadService) {
        this.couponReadService = couponReadService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons(MemberAuth memberAuth) {
        List<CouponResultDto> couponResultDtos = couponReadService.findByMember(memberAuth);
        List<CouponResponse> couponResponses = couponResultDtos.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(couponResponses);
    }

}
