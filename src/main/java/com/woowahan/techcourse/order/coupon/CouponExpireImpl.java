package com.woowahan.techcourse.order.coupon;

import com.woowahan.techcourse.coupon.application.CouponCommandService;
import com.woowahan.techcourse.coupon.application.dto.CouponExpireRequestDto;
import com.woowahan.techcourse.order.domain.CouponExpire;
import com.woowahan.techcourse.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class CouponExpireImpl implements CouponExpire {

    private final CouponCommandService couponCommandService;

    public CouponExpireImpl(CouponCommandService couponCommandService) {
        this.couponCommandService = couponCommandService;
    }

    @Override
    public void makeExpired(Order order) {
        CouponExpireRequestDto request = new CouponExpireRequestDto(order.getMemberId(), order.getCouponIds());
        couponCommandService.expireCoupon(request);
    }
}
