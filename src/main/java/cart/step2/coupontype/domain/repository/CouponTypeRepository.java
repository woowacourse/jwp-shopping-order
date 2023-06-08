package cart.step2.coupontype.domain.repository;

import cart.step2.coupontype.domain.CouponType;

import java.util.List;

public interface CouponTypeRepository {

    List<CouponType> findAll();

    CouponType findById(final Long couponTypeId);

}
