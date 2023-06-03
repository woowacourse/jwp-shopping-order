package cart.application;

import cart.dao.CouponDao;
import cart.domain.Amount;
import cart.domain.Coupon;
import cart.dto.SaveCouponRequest;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

  private final CouponDao couponDao;

  public CouponService(CouponDao couponDao) {
    this.couponDao = couponDao;
  }

  public Long createCoupon(final SaveCouponRequest request) {
    return couponDao.createCoupon(
        new Coupon(request.getName(),
            new Amount(request.getDiscountAmount()),
            new Amount(request.getMinAmount())));
  }

}
