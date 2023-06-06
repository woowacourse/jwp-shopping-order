package com.woowahan.techcourse.coupon.domain;

import com.woowahan.techcourse.coupon.exception.CouponException;
import java.util.ArrayList;
import java.util.List;

public class CouponMember {

    private final long memberId;
    private final List<Coupon> coupons;

    public CouponMember(long memberId, List<Coupon> coupons) {
        this.memberId = memberId;
        this.coupons = new ArrayList<>(coupons);
    }

    public boolean hasCoupon(Coupon coupon) {
        return coupons.contains(coupon);
    }

    public void addCoupon(Coupon coupon) {
        if (hasCoupon(coupon)) {
            throw new CouponException("멤버가 이미 쿠폰을 가지고 있습니다");
        }
        coupons.add(coupon);
    }

    public long getMemberId() {
        return memberId;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void expireCouponIds(List<Long> couponIds) {
        if (notHasAllCouponIds(couponIds)) {
            throw new CouponException("존재하지 않는 쿠폰이 있습니다.");
        }
        coupons.removeIf(coupon -> couponIds.contains(coupon.getCouponId()));
    }

    public boolean notHasAllCouponIds(List<Long> couponIds) {
        return !hasAllCouponIds(couponIds);
    }

    public boolean hasAllCouponIds(List<Long> couponIds) {
        return couponIds.stream()
                .allMatch(this::hasCouponId);
    }

    private boolean hasCouponId(Long couponId) {
        return coupons.stream()
                .anyMatch(coupon -> coupon.getCouponId().equals(couponId));
    }
}
