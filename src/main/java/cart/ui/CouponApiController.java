package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.AllCouponResponse;
import cart.dto.MemberCouponRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {
    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> issueCoupon(
            Member member,
            @PathVariable Long couponId,
            @RequestBody MemberCouponRequest request
    ) {
        couponService.issueCouponToMemberByCouponId(couponId, request, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<AllCouponResponse> findAllCoupon() {
        AllCouponResponse allCouponResponse = couponService.findAllCoupon();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allCouponResponse);
    }
}
