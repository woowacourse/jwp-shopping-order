package cart.ui;

import cart.application.CouponService;
import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.coupon.CouponRequest;
import cart.dto.coupon.CouponResponse;
import cart.domain.member.MemberCoupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        List<MemberCoupon> coupons = member.getCoupons();
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupon")
    public ResponseEntity<Void> addCoupon(@Valid @RequestBody Long couponId, Member member) {
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
