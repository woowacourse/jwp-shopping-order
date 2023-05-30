package coupon.application;

import coupon.application.dto.CalculateActualPriceRequestDto;
import coupon.application.dto.CalculateActualPriceResponseDto;
import coupon.db.dao.CouponQueryDao;
import coupon.domain.Coupon;
import coupon.domain.Coupons;
import coupon.domain.Money;
import coupon.domain.Order;
import coupon.exception.CouponNotFoundException;
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
