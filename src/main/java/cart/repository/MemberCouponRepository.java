package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.MemberCoupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final MemberDao memberDao;
    private final CouponDao couponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final MemberDao memberDao, final CouponDao couponDao) {
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
        this.couponDao = couponDao;
    }

    public Optional<MemberCoupon> findByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        final Optional<MemberCouponEntity> mayBeMemberCouponEntity = memberCouponDao.findByMemberIdAndCouponId(memberId, couponId);
        if (mayBeMemberCouponEntity.isEmpty()) {
            return Optional.empty();
        }
        final MemberCouponEntity entity = mayBeMemberCouponEntity.get();
        final MemberEntity memberEntity = memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final CouponEntity couponEntity = couponDao.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);

        return Optional.of(new MemberCoupon(entity.getId(), memberEntity.toDomain(), couponEntity.toDomain(), entity.isUsed()));
    }

    public MemberCoupon save(final MemberCoupon memberCoupon) {
        if (Objects.isNull(memberCoupon.getId())) {
            final MemberCouponEntity entity = memberCouponDao.insert(MemberCouponEntity.from(memberCoupon));
            return new MemberCoupon(entity.getId(), memberCoupon.getMember(), memberCoupon.getCoupon(), memberCoupon.isUsed());
        }
        memberCouponDao.update(MemberCouponEntity.from(memberCoupon));
        return memberCoupon;
    }
}
