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
    public Long save(Member member, Long couponId) {
        return memberCouponDao.save(new MemberCouponEntity(couponId, member.getId(), true));
    }

    @Override
    public List<Coupon> findByMemberId(Member member) {
        return memberCouponDao.findAllByMemberId(member.getId()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Coupon findAvailableCouponByIdAndMemberId(Member member, Long couponId) {
        return toDomain(memberCouponDao.findAvailableCouponByIdAndMemberId(member.getId(), couponId).orElse(CouponEntity.EMPTY));
    }

    @Override
    public boolean checkById(Long couponId) {
        return couponDao.checkById(couponId);
    }

    @Override
    public boolean checkByCouponIdAndMemberId(Long couponId, Long memberId) {
        return memberCouponDao.checkByCouponIdAndMemberId(couponId, memberId);
    }

    private Coupon toDomain(CouponEntity entity) {
        CouponTypes couponType = DiscountType.from(entity.getDiscountType());
        return new Coupon(entity.getId(), entity.getName(), couponType,
                entity.getMinimumPrice(), entity.getDiscountPrice(), entity.getDiscountRate());
    }
}
