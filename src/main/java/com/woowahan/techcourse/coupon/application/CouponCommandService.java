package com.woowahan.techcourse.coupon.application;

import com.woowahan.techcourse.coupon.application.dto.CouponExpireRequest;
import com.woowahan.techcourse.coupon.db.dao.CouponDao;
import com.woowahan.techcourse.coupon.db.dao.CouponMemberDao;
import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.CouponMember;
import com.woowahan.techcourse.coupon.exception.CouponMemberNotFoundException;
import com.woowahan.techcourse.coupon.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponCommandService {

    private final CouponMemberDao couponMemberDao;
    private final CouponDao couponDao;

    public CouponCommandService(CouponMemberDao couponMemberDao, CouponDao couponDao) {
        this.couponMemberDao = couponMemberDao;
        this.couponDao = couponDao;
    }

    public void addCoupon(Long couponId, Long memberId) {
        Coupon coupon = couponDao.findById(couponId).orElseThrow(CouponNotFoundException::new);
        CouponMember couponMember = couponMemberDao.findByMemberId(memberId)
                .orElseThrow(CouponMemberNotFoundException::new);
        couponMember.addCoupon(coupon);
        couponMemberDao.update(couponMember);
    }

    public void expireCoupon(CouponExpireRequest requestDto) {
        CouponMember couponMember = couponMemberDao.findByMemberId(requestDto.getMemberId())
                .orElseThrow(CouponMemberNotFoundException::new);
        couponMember.expireCouponIds(requestDto.getCouponIds());
        couponMemberDao.update(couponMember);
    }
}
