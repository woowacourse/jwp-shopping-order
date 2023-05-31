package com.woowahan.techcourse.coupon.application;

import com.woowahan.techcourse.coupon.application.dto.CouponExpireRequestDto;
import com.woowahan.techcourse.coupon.db.dao.CouponDao;
import com.woowahan.techcourse.coupon.db.dao.CouponMemberDao;
import com.woowahan.techcourse.coupon.exception.CouponException;
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
        if (!couponDao.exists(couponId)) {
            throw new CouponException("존재하지 않는 쿠폰입니다.");
        }
        if (couponMemberDao.exists(couponId, memberId)) {
            throw new CouponException("멤버가 이미 쿠폰을 가지고 있습니다");
        }
        couponMemberDao.insert(couponId, memberId);
    }

    public void expireCoupon(CouponExpireRequestDto requestDto) {
        int count = couponMemberDao.countByMemberIdAndCouponIds(requestDto.getMemberId(), requestDto.getCouponIds());
        if (count != requestDto.getCouponIds().size()) {
            throw new CouponException("존재하지 않는 쿠폰이 있습니다.");
        }
        couponMemberDao.deleteByMemberId(requestDto.getMemberId(), requestDto.getCouponIds());
    }
}
