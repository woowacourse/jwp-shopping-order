package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponTypes;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.MemberCouponRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberCouponRepositoryImpl implements MemberCouponRepository {
    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;

    public MemberCouponRepositoryImpl(MemberCouponDao memberCouponDao, CouponDao couponDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
    }

    @Override
    public Coupon findAvailableCouponByMember(Member member, Long couponId) {
        return toDomain(memberCouponDao.findAvailableCouponByMember(member.getId(), couponId).orElse(CouponEntity.EMPTY));
    }

    @Override
    public void changeUserUsedCouponAvailability(Coupon coupon) {
        memberCouponDao.changeUserUsedCouponAvailability(coupon.getId());
    }

    @Override
    public List<Coupon> findMemberCoupons(Member member) {
        return memberCouponDao.findCouponByMemberId(member.getId()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void changeUserUnUsedCouponAvailability(Member member, Long memberCouponId) {
        if (!memberCouponDao.checkMemberCouponById(member.getId(), memberCouponId)) {
            memberCouponDao.changeUserUnUsedCouponAvailability(memberCouponId);
        }
    }

    @Override
    public Coupon publishBonusCoupon(Long id, Member member) {
        Coupon coupon = toDomain(couponDao.findCouponByName(toEntity(Coupon.bonusCoupon())).orElseThrow(() -> new CouponException("보너스 쿠폰이 없습니다.")));
        Long userCouponId = memberCouponDao.createUserCoupon(new MemberCouponEntity(coupon.getId(), member.getId(), true));
        return new Coupon(userCouponId, coupon.getName(),
                DiscountType.from(coupon.getCouponTypes().getCouponTypeName()),
                coupon.getMinimumPrice(), coupon.getDiscountPrice(), coupon.getDiscountRate());
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
