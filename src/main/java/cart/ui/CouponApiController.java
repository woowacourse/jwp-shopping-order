package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.ui.dto.response.CouponDiscountResponse;
import cart.ui.dto.response.CouponResponse;
import cart.ui.dto.response.PossibleCouponResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findAllCoupons(final Member member) {
        final List<CouponResponse> couponResponses = couponService.findAllCouponByMember(member);
        return ResponseEntity.ok().body(couponResponses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> registerCouponToMember(final Member member, @PathVariable final Long id) {
        couponService.registerCouponToMember(id, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/discount")
    public ResponseEntity<CouponDiscountResponse> calculateDiscount(@PathVariable final Long id,
        @RequestParam final Integer total) {
        final CouponDiscountResponse response = couponService.calculateCouponDiscount(id, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    private ResponseEntity<List<PossibleCouponResponse>> findActiveCouponByMember(final Member member,
        @RequestParam final int total) {
        final List<PossibleCouponResponse> responses = couponService.findPossibleCouponByMember(member, total);
        return ResponseEntity.ok(responses);
    }
}
