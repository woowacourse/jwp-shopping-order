package cart.step2.coupontype.persist;

import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.CouponTypeEntity;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import cart.step2.coupontype.exception.CouponTypeNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponTypeRepositoryMapper implements CouponTypeRepository {

    private final CouponTypeDao couponTypeDao;

    public CouponTypeRepositoryMapper(final CouponTypeDao couponTypeDao) {
        this.couponTypeDao = couponTypeDao;
    }

    @Override
    public List<CouponType> findAll() {
        return couponTypeDao.findAll().stream()
                .map(CouponTypeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CouponType findById(final Long couponTypeId) {
        return couponTypeDao.findById(couponTypeId)
                .orElseThrow(() -> CouponTypeNotFoundException.THROW)
                .toDomain();
    }

}
