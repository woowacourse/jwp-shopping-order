package cart.controller;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponReissueRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
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
}
