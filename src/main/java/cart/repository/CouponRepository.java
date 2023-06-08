package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.coupon.StrategyFactory;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;
    private final StrategyFactory strategyFactory;

    public CouponRepository(final CouponDao couponDao, final MemberCouponDao memberCouponDao, final StrategyFactory strategyFactory) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.strategyFactory = strategyFactory;
    }

    public List<Coupon> findAllCoupon() {
        List<CouponEntity> couponEntities = couponDao.findAll();
        return couponEntities.stream()
                .map(this::toCoupon)
                .collect(Collectors.toList());
    }

    private Coupon toCoupon(final CouponEntity entity) {
        return new Coupon(
                entity.getId(),
                entity.getName(),
                new Discount(
                        strategyFactory.findStrategy(entity.getDiscountType()),
                        entity.getAmount()
                )
        );
    }

    public Long createCoupon(Coupon coupon) {
        return couponDao.create(CouponEntity.from(coupon));
    }

    public void deleteCoupon(Long id) {
        couponDao.delete(id);
    }

    public List<MemberCoupon> findUnUsedMemberCouponByMember(Member member) {
        List<MemberCouponEntity> entities = memberCouponDao.findByMemberId(member.getId());
        return entities.stream()
                .map(this::toMemberCoupon)
                .filter(MemberCoupon::isUnUsed)
                .collect(Collectors.toList());
    }

    private MemberCoupon toMemberCoupon(final MemberCouponEntity entity) {
        return new MemberCoupon(
                entity.getId(),
                toCoupon(entity.getCoupon()),
                entity.isUsed()
        );
    }

    public Long createMemberCoupons(Member member, Long couponId) {
        return memberCouponDao.create(member.getId(), couponId);
    }

    public List<MemberCoupon> findMemberCouponsByIds(List<Long> ids) {
        List<MemberCouponEntity> entities = memberCouponDao.findByIds(ids);
        return entities.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    public List<MemberCoupon> findMemberCouponsByMemberId(Long memberId) {
        List<MemberCouponEntity> entities = memberCouponDao.findByMemberId(memberId);
        return entities.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    public void updateMemberCoupon(List<MemberCoupon> memberCoupons, Long memberId) {
        List<MemberCouponEntity> entities = memberCoupons.stream()
                .map(this::toMemberCouponEntity)
                .collect(Collectors.toList());
        memberCouponDao.updateCoupon(entities, memberId);
    }

    private MemberCouponEntity toMemberCouponEntity(final MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                new CouponEntity(
                        memberCoupon.getCoupon().getId(),
                        memberCoupon.getCoupon().getName(),
                        memberCoupon.getCoupon().getDiscount().getStrategy().getStrategyName().name(),
                        memberCoupon.getCoupon().getDiscount().getAmount()
                ),
                memberCoupon.isUsed()
        );
    }
}
