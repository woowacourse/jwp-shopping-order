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
    public Coupon findAvailableCouponByIdAndMemberId(Member member, Long couponId) {
        return toDomain(memberCouponDao.findAvailableCouponByIdAndMemberId(member.getId(), couponId).orElse(CouponEntity.EMPTY));
    }


    @Override
    public List<Coupon> findAllByMemberId(Member member) {
        return memberCouponDao.findAllByMemberId(member.getId()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUnUsedCouponAvailability(Member member, Long memberCouponId) {
        if (!memberCouponDao.checkByCouponIdAndMemberId(member.getId(), memberCouponId)) {
            memberCouponDao.updateUnUsedCouponAvailabilityById(memberCouponId);
        }
    }

    @Override
    public Coupon saveBonusCoupon(Long id, Member member) {
        Coupon coupon = toDomain(couponDao.findByName(toEntity(Coupon.BONUS_COUPON)).orElseThrow(() -> new CouponException("보너스 쿠폰이 없습니다.")));
        Long userCouponId = memberCouponDao.save(new MemberCouponEntity(coupon.getId(), member.getId(), true));
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
            return Coupon.EMPTY;
        }
        CouponTypes couponType = DiscountType.from(entity.getDiscountType());
        return new Coupon(entity.getId(), entity.getName(), couponType,
                entity.getMinimumPrice(), entity.getDiscountPrice(), entity.getDiscountRate());
    }
}
