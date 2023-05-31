package cart.ui;

import cart.application.CouponService;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.coupon.CouponRequest;
import cart.dto.coupon.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CouponController {

    private final CouponService couponService;
    private final MemberCouponDao memberCouponDao;

    public CouponController(final CouponService couponService, final MemberCouponDao memberCouponDao) {
        this.couponService = couponService;
        this.memberCouponDao = memberCouponDao;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showAllCoupons(Member member) {
        List<Coupon> coupons = member.getCoupons();
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupons")
    public ResponseEntity<Void> addCoupon(@RequestBody Long couponId, Member member) {
        memberCouponDao.create(couponId, member.getId());
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
    public ResponseEntity<Void> addCoupon(@RequestBody CouponRequest request) {
        Long couponId = couponService.create(request);
        return ResponseEntity.created(URI.create("/coupon/" + couponId)).build();
    }

    @DeleteMapping("/admin/coupon/{id}")
    public ResponseEntity<Void> deleteCoupons(@PathVariable final Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
