package cart.controller;

import cart.application.CouponProvider;
import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponReissueRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponTypeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @PostMapping
    public ResponseEntity<Void> issueCoupon(final Member member, @RequestBody final CouponIssueRequest request) {
        final Long couponId = couponService.issueCoupon(member, request);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{couponId}")
                .buildAndExpand(couponId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> reissueCoupon(@PathVariable final Long couponId, @RequestBody final CouponReissueRequest request) {
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

}
