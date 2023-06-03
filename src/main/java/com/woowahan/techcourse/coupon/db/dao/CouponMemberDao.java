package com.woowahan.techcourse.coupon.db.dao;

import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.CouponMember;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponMemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final CouponDao couponDao;

    public CouponMemberDao(JdbcTemplate jdbcTemplate, CouponDao couponDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.couponDao = couponDao;
    }

    public void insert(Long couponId, Long memberId) {
        jdbcTemplate.update("INSERT INTO coupon_member (coupon_id, member_id) VALUES (?, ?)", couponId, memberId);
    }

    public void delete(Long couponId, Long memberId) {
        jdbcTemplate.update("DELETE FROM coupon_member WHERE coupon_id = ? AND member_id = ?", couponId, memberId);
    }

    public Optional<CouponMember> findByMemberId(Long memberId) {
        List<Coupon> coupons = couponDao.findAllByMemberId(memberId);
        return Optional.of(new CouponMember(memberId, coupons));
    }

    public void update(CouponMember couponMember) {
        List<Coupon> coupons = couponDao.findAllByMemberId(couponMember.getMemberId());
        List<Coupon> needToAddCoupon = calculateNeedToAddCoupons(couponMember, coupons);
        List<Coupon> needToDeleteCoupon = calculateNeedToDeleteCoupons(couponMember, coupons);
        executeUpdateQuery(couponMember, needToAddCoupon, needToDeleteCoupon);
    }

    private List<Coupon> calculateNeedToAddCoupons(CouponMember couponMember, List<Coupon> coupons) {
        return couponMember.getCoupons()
                .stream()
                .filter(coupon -> !coupons.contains(coupon))
                .collect(Collectors.toList());
    }

    private List<Coupon> calculateNeedToDeleteCoupons(CouponMember couponMember, List<Coupon> coupons) {
        return coupons.stream()
                .filter(coupon -> !couponMember.getCoupons().contains(coupon))
                .collect(Collectors.toList());
    }

    private void executeUpdateQuery(CouponMember couponMember, List<Coupon> needToAddCoupon,
            List<Coupon> needToDeleteCoupon) {
        needToAddCoupon.forEach(coupon -> insert(coupon.getCouponId(), couponMember.getMemberId()));
        needToDeleteCoupon.forEach(coupon -> delete(coupon.getCouponId(), couponMember.getMemberId()));
    }
}
