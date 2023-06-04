package cart.ui;

import cart.application.CouponService;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponRequest;
import cart.dto.coupon.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showAllCoupons(Member member) {
        List<MemberCoupon> memberCoupons = couponService.findUnUsedMemberCouponByMember(member);
        List<CouponResponse> couponResponses = memberCoupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupon")
    public ResponseEntity<Void> addCoupon(@Valid @RequestBody CouponCreateRequest request, Member member) {
        couponService.createMemberCoupons(member, request.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/coupons")
    public ResponseEntity<List<CouponResponse>> showAllCoupons() {
        List<Coupon> coupons = couponService.findAll();
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/admin/coupon")
    public ResponseEntity<Void> addCoupon(@Valid @RequestBody CouponRequest request) {
        Long couponId = couponService.create(request);
        return ResponseEntity.created(URI.create("/coupon/" + couponId)).build();
    }

    @DeleteMapping("/admin/coupon/{id}")
    public ResponseEntity<Void> deleteCoupons(@PathVariable final Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
