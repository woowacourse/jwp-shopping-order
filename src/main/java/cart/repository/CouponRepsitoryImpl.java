package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponTypes;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.CouponRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponException;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepsitoryImpl implements CouponRepository {
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepsitoryImpl(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Long publishUserCoupon(Member member, Long couponId) {
        if (!couponDao.checkCouponById(couponId)) {
            throw new CouponException("해당 쿠폰을 찾을 수 없습니다.");
        }

        if(memberCouponDao.checkMemberCouponById(member.getId(),couponId)){
            throw new CouponException("이미 존재하는 쿠폰입니다.");
        }

        return memberCouponDao.createUserCoupon(new MemberCouponEntity(couponId, member.getId(), true));
    }



    private CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(coupon.getName(),
                coupon.getCouponTypes().getCouponTypeName(),
                coupon.getMinimumPrice(),
                coupon.getDiscountPrice(),
                coupon.getDiscountRate());
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