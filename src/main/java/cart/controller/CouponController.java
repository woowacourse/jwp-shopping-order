package cart.controller;

import cart.domain.Member;
import cart.dto.CouponReissueRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponTypeResponse;
import cart.service.coupon.CouponProvider;
import cart.service.coupon.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponProvider couponProvider;

    public CouponController(final CouponService couponService, final CouponProvider couponProvider) {
        this.couponService = couponService;
        this.couponProvider = couponProvider;
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> issueCoupon(final Member member, @PathVariable Long couponId) {
        final Long memberCouponId = couponService.issueCoupon(member, couponId);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{couponId}")
                .buildAndExpand(memberCouponId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> reissueCoupon(@PathVariable final Long couponId, @Valid @RequestBody final CouponReissueRequest request) {
        couponService.reissueCoupon(couponId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponTypeResponse>> getAllCoupons() {
        final List<CouponTypeResponse> couponResponses = couponProvider.findCouponAll();
        return ResponseEntity.ok(couponResponses);
    }

    @GetMapping("/member")
    public ResponseEntity<List<CouponResponse>> getMemberCoupons(final Member member) {
        final List<CouponResponse> couponResponses = couponProvider.findCouponByMember(member);
        return ResponseEntity.ok(couponResponses);
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable final Long couponId, final Member member) {
        couponService.deleteCoupon(couponId, member.getId());
        return ResponseEntity.noContent().build();
    }
}
