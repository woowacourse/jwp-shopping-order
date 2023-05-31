package cart.persistence.repository;

import static cart.persistence.mapper.MemberCouponMapper.convertMemberCoupon;

import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.exception.DBException;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberCouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepositoryImpl implements MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepositoryImpl(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public void save(final Long memberId, final MemberCoupon memberCoupon) {
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(memberId,
            memberCoupon.getCoupon().getCouponId(),
            memberCoupon.getIssuedAt(), memberCoupon.getExpiredAt(), false);
        memberCouponDao.insert(memberCouponEntity);
    }

    @Override
    public boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        return memberCouponDao.existByMemberIdAndCouponId(memberId, couponId);
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        final MemberCouponDto memberCouponDto = memberCouponDao.findByMemberIdAndCouponId(memberId, couponId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
        return convertMemberCoupon(memberCouponDto);
    }

    @Override
    public void updateUsed(final Long memberId, final Long couponId) {
        int updatedCount = memberCouponDao.updateUsed(memberId, couponId);
        if (updatedCount != 1) {
            throw new DBException(ErrorCode.DB_UPDATE_ERROR);
        }
    }
}
