package com.woowahan.techcourse.coupon.application;

import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequestDto;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceResponseDto;
import com.woowahan.techcourse.coupon.db.dao.CouponDao;
import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.Coupons;
import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.Order;
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

    public CalculateActualPriceResponseDto calculateActualPrice(
            CalculateActualPriceRequestDto calculateActualPriceRequestDto) {
        Coupons coupons = getCoupons(calculateActualPriceRequestDto);

        Order targetOrder = calculateActualPriceRequestDto.getOrderRequest().toOrder();

        Money money = coupons.calculateActualPrice(targetOrder);

        return new CalculateActualPriceResponseDto(money.getValue());
    }

    private Coupons getCoupons(CalculateActualPriceRequestDto calculateActualPriceRequestDto) {
        List<Coupon> coupons = couponDao.findAllByIds(calculateActualPriceRequestDto.getCouponIds());
        if (coupons.size() != calculateActualPriceRequestDto.getCouponIds().size()) {
            throw new CouponNotFoundException();
        }
        return new Coupons(coupons);
    }
}
