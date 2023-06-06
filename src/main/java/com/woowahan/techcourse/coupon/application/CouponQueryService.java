package com.woowahan.techcourse.coupon.application;

import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequest;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceResponse;
import com.woowahan.techcourse.coupon.db.dao.CouponDao;
import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.Coupons;
import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.OriginalAmount;
import com.woowahan.techcourse.coupon.exception.CouponNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

    private final CouponDao couponDao;

    public CouponQueryService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<Coupon> findAllCoupons() {
        return couponDao.findAll();
    }

    public List<Coupon> findAllCouponsByMemberId(Long memberId) {
        return couponDao.findAllByMemberId(memberId);
    }

    public CalculateActualPriceResponse calculateActualPrice(
            CalculateActualPriceRequest calculateActualPriceRequest) {
        Coupons coupons = findCoupons(calculateActualPriceRequest.getCouponIds());

        OriginalAmount originalAmount = new OriginalAmount(
                calculateActualPriceRequest.getOrderRequest().getOriginalPrice());

        Money money = coupons.calculateActualPrice(originalAmount);

        return new CalculateActualPriceResponse(money.getValue());
    }

    private Coupons findCoupons(List<Long> couponIds) {
        List<Coupon> coupons = couponDao.findAllByIds(couponIds);
        if (coupons.size() != couponIds.size()) {
            throw new CouponNotFoundException();
        }
        return new Coupons(coupons);
    }
}
