package cart.repository;

import cart.dao.MemberCouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponTypes;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.MemberCouponRepository;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepositoryImpl implements MemberCouponRepository {
    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepositoryImpl(MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Coupon findAvailableCouponByMember(Member member, Long couponId) {
        return toDomain(memberCouponDao.findAvailableCouponByMember(member.getId(), couponId).orElse(CouponEntity.EMPTY));
    }

    @Override
    public void changeUserUsedCouponAvailability(Coupon coupon) {
        memberCouponDao.changeUserUsedCouponAvailability(coupon.getId());
    }

    private Coupon toDomain(CouponEntity entity) {
        if (entity.equals(CouponEntity.EMPTY)) {
            return Coupon.empty();
        }
        CouponTypes couponType = DiscountType.from(entity.getDiscountType());
        return new Coupon(entity.getId(), entity.getName(), couponType,
                entity.getMinimumPrice(), entity.getDiscountPrice(), entity.getDiscountRate());
    }
}
