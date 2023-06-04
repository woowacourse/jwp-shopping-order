package cart.infrastructure.repository;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.repository.CouponRepository;
import cart.infrastructure.dao.CouponDao;
import cart.infrastructure.dao.CouponIssueConditionDao;
import cart.infrastructure.entity.CouponEntity;
import cart.infrastructure.entity.CouponIssueConditionEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCouponRepository implements CouponRepository {

    private final CouponDao couponDao;
    private final CouponIssueConditionDao couponIssueConditionDao;

    public JdbcCouponRepository(CouponDao couponDao, CouponIssueConditionDao couponIssueConditionDao) {
        this.couponDao = couponDao;
        this.couponIssueConditionDao = couponIssueConditionDao;
    }

    @Override
    public Coupon save(Coupon coupon) {
        Long id = couponDao.save(toEntity(coupon));
        return new Coupon(
                id,
                coupon.getName(),
                coupon.getCouponType(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice()
        );
    }

    @Override
    public IssuableCoupon saveIssuableCoupon(IssuableCoupon issuableCoupon) {
        Long id = couponIssueConditionDao.save(toEntity(issuableCoupon));
        return new IssuableCoupon(id, issuableCoupon.getCoupon(), issuableCoupon.getMoney());
    }

    private CouponIssueConditionEntity toEntity(IssuableCoupon issuableCoupon) {
        return new CouponIssueConditionEntity(
                issuableCoupon.getCoupon().getId(),
                issuableCoupon.getMoney().getValue()
        );
    }

    @Override
    public List<IssuableCoupon> findAllIssuable() {
        List<CouponIssueConditionEntity> couponIssueConditionEntities = couponIssueConditionDao.findAll();

        return couponIssueConditionEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

    }

    private IssuableCoupon toDomain(CouponIssueConditionEntity couponIssueCondition) {
        return new IssuableCoupon(
                couponIssueCondition.getId(),
                toCoupon(couponIssueCondition),
                new Money(couponIssueCondition.getMinIssuePrice())
        );
    }

    private Coupon toCoupon(CouponIssueConditionEntity it) {
        return couponDao.findById(it.getCouponId())
                .map(CouponEntity::toDomain)
                .orElseThrow();
    }

    private CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getName(),
                coupon.getCouponType().name(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice().getValue()
        );
    }
}
