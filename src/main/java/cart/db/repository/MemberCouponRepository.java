package cart.db.repository;

import cart.db.dao.MemberCouponDao;
import cart.db.entity.MemberCouponDetailEntity;
import cart.db.entity.MemberCouponEntity;
import cart.db.mapper.MemberCouponMapper;
import cart.domain.coupon.MemberCoupon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    public void save(final MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = MemberCouponMapper.toEntity(memberCoupon);
        memberCouponDao.create(memberCouponEntity);
    }

    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        List<MemberCouponDetailEntity> memberCouponDetailEntities = memberCouponDao.findAllByMemberId(memberId);
        return MemberCouponMapper.toDomain(memberCouponDetailEntities);
    }

    public boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        return memberCouponDao.existByMemberIdAndCouponId(memberId, couponId);
    }
}
