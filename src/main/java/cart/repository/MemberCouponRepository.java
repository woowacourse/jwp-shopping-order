package cart.repository;

import cart.db.dao.MemberCouponDao;
import cart.db.entity.MemberCouponEntity;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberCouponRepository {

    private final CouponRepository couponRepository;
    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(
            CouponRepository couponRepository,
            MemberCouponDao memberCouponDao
    ) {
        this.couponRepository = couponRepository;
        this.memberCouponDao = memberCouponDao;
    }

    public Long insert(Member member, Coupon coupon) {
        return memberCouponDao.insert(new MemberCouponEntity(false, member.getId(), coupon.getId()));
    }

    public List<MemberCoupon> findAllByMemberId(Long memberId) {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAllByMemberId(memberId);
        return entitiesToDomains(memberCouponEntities);
    }

    private List<MemberCoupon> entitiesToDomains(final List<MemberCouponEntity> memberCouponEntities) {
        List<Long> couponIds = memberCouponEntities.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(Collectors.toList());
        List<Coupon> coupons = couponRepository.findByIds(couponIds);

        return combineMemberCouponEntitiesAndCoupons(memberCouponEntities, coupons);
    }

    private List<MemberCoupon> combineMemberCouponEntitiesAndCoupons(List<MemberCouponEntity> entities, List<Coupon> coupons) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();
        for (int index = 0; index < entities.size(); index++) {
            MemberCouponEntity memberCouponEntity = entities.get(index);
            Coupon coupon = coupons.get(index);
            memberCoupons.add(combineMemberCouponEntityAndCoupon(memberCouponEntity, coupon));
        }
        return memberCoupons;
    }

    private MemberCoupon combineMemberCouponEntityAndCoupon(MemberCouponEntity entity, Coupon coupon) {
        return new MemberCoupon(entity.getId(), entity.getMemberId(), coupon, entity.getUsed());
    }

    public MemberCoupon findByIdForUpdate(Long id) {
        MemberCouponEntity memberCouponEntity = memberCouponDao.findByIdForUpdate(id);
        Coupon coupon = couponRepository.findById(memberCouponEntity.getCouponId());
        return combineMemberCouponEntityAndCoupon(memberCouponEntity, coupon);
    }

    public List<MemberCoupon> findAllByIdForUpdate(List<Long> memberCouponIds) {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAllByIdForUpdate(memberCouponIds);
        return entitiesToDomains(memberCouponEntities);
    }

    public void updateCouponStatus(MemberCoupon memberCoupon) {
        memberCouponDao.updateCouponStatus(MemberCouponEntity.of(memberCoupon));
    }

    public List<MemberCoupon> findAllById(List<Long> memberCouponIds) {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAllById(memberCouponIds);
        return entitiesToDomains(memberCouponEntities);
    }
}
