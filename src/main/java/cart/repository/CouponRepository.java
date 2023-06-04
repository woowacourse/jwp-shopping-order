package cart.repository;

import cart.dao.CouponDao;
import cart.dao.CouponIssueConditionDao;
import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.IssuableCoupon;
import cart.entity.CouponEntity;
import cart.entity.CouponIssueConditionEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final CouponIssueConditionDao couponIssueConditionDao;

    public CouponRepository(CouponDao couponDao, CouponIssueConditionDao couponIssueConditionDao) {
        this.couponDao = couponDao;
        this.couponIssueConditionDao = couponIssueConditionDao;
    }

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
