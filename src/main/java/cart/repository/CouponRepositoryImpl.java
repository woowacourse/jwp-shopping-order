package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponTypes;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.CouponRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepositoryImpl(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Coupon save(Long memberId, Long couponId) {
        Long savedId = memberCouponDao.save(new MemberCouponEntity(couponId, memberId, true));
        CouponEntity couponEntity = memberCouponDao.findById(savedId)
                .orElseThrow(() -> new CouponException("잘못된 쿠폰입니다."));
        return toDomain(couponEntity);
    }

    @Override
    public List<Coupon> findByMemberId(Long memberId) {
        return memberCouponDao.findAllByMemberId(memberId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findAvailableCouponByIdAndMemberId(Long couponId, Long memberId) {
        if (memberCouponDao.existsByCouponIdAndMemberId(couponId, memberId) || couponId == null) {
            return toDomain(memberCouponDao.findByIdAndMemberIdAndAvailable(couponId, memberId, true)
                    .orElse(CouponEntity.EMPTY));
        }
        return null;
    }

    @Override
    public boolean existsById(Long couponId) {
        return couponDao.existsById(couponId);
    }

    @Override
    public boolean existsByCouponIdAndMemberId(Long couponId, Long memberId) {
        return memberCouponDao.existsByCouponIdAndMemberId(couponId, memberId);
    }

    private Coupon toDomain(CouponEntity entity) {
        CouponTypes couponType = DiscountType.from(entity.getDiscountType());
        return new Coupon(entity.getId(), entity.getName(), couponType,
                entity.getMinimumPrice(), entity.getDiscountPrice(), entity.getDiscountRate());
    }
}
