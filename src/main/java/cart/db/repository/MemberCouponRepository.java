package cart.db.repository;

import cart.db.dao.MemberCouponDao;
import cart.db.entity.MemberCouponDetailEntity;
import cart.db.entity.MemberCouponEntity;
import cart.domain.coupon.MemberCoupon;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.db.mapper.MemberCouponMapper.toDomain;
import static cart.db.mapper.MemberCouponMapper.toEntity;
import static cart.exception.ErrorCode.INVALID_COUPON_ID;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    public void save(final MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = toEntity(memberCoupon);
        memberCouponDao.create(memberCouponEntity);
    }

    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        List<MemberCouponDetailEntity> memberCouponDetailEntities = memberCouponDao.findAllByMemberId(memberId);
        return toDomain(memberCouponDetailEntities);
    }

    public MemberCoupon findByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        MemberCouponDetailEntity memberCouponDetailEntity = memberCouponDao.findByMemberIdAndCouponId(memberId, couponId)
                .orElseThrow(() -> new BadRequestException(INVALID_COUPON_ID));
        return toDomain(memberCouponDetailEntity);
    }

    public void update(final MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = toEntity(memberCoupon);
        memberCouponDao.update(memberCouponEntity);
    }

    public boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        return memberCouponDao.existByMemberIdAndCouponId(memberId, couponId);
    }

}
