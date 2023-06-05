package cart.domain.coupon;

import java.util.List;

public interface SerialNumberRepository {

	void generateCouponSerialNumber(Long couponId, List<SerialNumber> serialNumbers);

	void issueCoupon(Long serialNumberId);
}
