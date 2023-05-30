package cart.persistence.repository;

import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.persistence.dao.MemberCouponDao;
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
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(memberId, memberCoupon.getCoupon().getId(),
            memberCoupon.getIssuedDate(), memberCoupon.getExpiredDate(), false);
        memberCouponDao.insert(memberCouponEntity);
    }
}
