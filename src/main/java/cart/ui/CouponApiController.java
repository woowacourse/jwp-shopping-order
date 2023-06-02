package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
