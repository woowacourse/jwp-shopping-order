package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.cart.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberCouponNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final CouponDao couponDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
    }

    public void saveAll(final List<MemberCoupon> memberCoupons) {
        final List<MemberCouponEntity> memberCouponEntities = memberCoupons.stream()
                .map(MemberCouponEntity::from)
                .collect(toList());

        memberCouponDao.insertAll(memberCouponEntities);
    }

    public MemberCoupon save(final MemberCoupon memberCoupon) {
        final MemberCouponEntity memberCouponEntity = MemberCouponEntity.from(memberCoupon);
        final MemberCouponEntity savedMemberCouponEntity = memberCouponDao.insert(memberCouponEntity);
        return new MemberCoupon(
                savedMemberCouponEntity.getId(),
                memberCoupon.getMemberId(),
                memberCoupon.getCoupon(),
                memberCoupon.isUsed()
        );
    }

    public Optional<MemberCoupon> findById(final Long id) {
        final MemberCouponEntity memberCouponEntity = memberCouponDao.findById(id)
                .orElseThrow(MemberCouponNotFoundException::new);

        final Coupon coupon = couponDao.findById(memberCouponEntity.getCouponId())
                .map(CouponEntity::toDomain)
                .orElseThrow(CouponNotFoundException::new);

        return Optional.of(new MemberCoupon(
                memberCouponEntity.getId(),
                memberCouponEntity.getMemberId(),
                coupon,
                memberCouponEntity.isUsed()
        ));
    }

    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        final List<Long> couponIds = memberCouponDao.findAllByUsedAndMemberId(false, memberId).stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(toList());
        return couponDao.findByIds(couponIds).stream()
                .map(CouponEntity::toDomain)
                .map(coupon -> new MemberCoupon(memberId, coupon))
                .collect(toList());
    }
}
