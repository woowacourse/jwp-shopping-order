package com.woowahan.techcourse.coupon.application;

import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequestDto;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceResponseDto;
import com.woowahan.techcourse.coupon.db.dao.CouponQueryDao;
import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.Coupons;
import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.Order;
import com.woowahan.techcourse.coupon.exception.CouponNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponQueryService {

    private final CouponQueryDao couponQueryDao;

    public CouponQueryService(CouponQueryDao couponQueryDao) {
        this.couponQueryDao = couponQueryDao;
    }

    public List<Coupon> findAllCoupons() {
        return couponQueryDao.findAll();
    }

    public List<Coupon> findAllCouponsByMemberId(Long memberId) {
        return couponQueryDao.findAllByMemberId(memberId);
    }

    public CalculateActualPriceResponseDto calculateActualPrice(
            CalculateActualPriceRequestDto calculateActualPriceRequestDto) {
        Coupons coupons = getCoupons(calculateActualPriceRequestDto);

        Order targetOrder = calculateActualPriceRequestDto.getOrderRequest().toOrder();

        Money money = coupons.calculateActualPrice(targetOrder);

        return new CalculateActualPriceResponseDto(money.getValue());
    }

    private Coupons getCoupons(CalculateActualPriceRequestDto calculateActualPriceRequestDto) {
        List<Coupon> coupons = couponQueryDao.findAllByIds(calculateActualPriceRequestDto.getCouponIds());
        if (coupons.size() != calculateActualPriceRequestDto.getCouponIds().size()) {
            throw new CouponNotFoundException();
        }
        return new Coupons(coupons);
    }
}
