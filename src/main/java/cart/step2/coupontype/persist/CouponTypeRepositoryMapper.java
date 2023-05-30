package cart.step2.coupontype.persist;

import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.CouponTypeEntity;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 종류입니다. 쿠폰 타입 ID가 일치하는지 확인해주세요."))
                .toDomain();
    }

}
