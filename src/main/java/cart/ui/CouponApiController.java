package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coupon", description = "쿠폰 API")
@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;


    public CouponApiController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "사용자에게 쿠폰 발급")
    @PostMapping
    public ResponseEntity<Void> issueCouponToMember(Member member, @RequestBody CouponIssueRequest couponIssueRequest) {
        couponService.issueCouponByIdToMember(couponIssueRequest.getCouponId(), member);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 쿠폰 조회")
    @GetMapping
    public ResponseEntity<CouponResponses> findAll() {
        return ResponseEntity.ok(couponService.findAll());
    }
}
