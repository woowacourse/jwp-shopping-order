package cart.repository;

import cart.dao.MemberCouponDao;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupons;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    public MemberCoupons findByMemberId(Long id) {
        return new MemberCoupons(memberCouponDao.findByMemberId(id));
    }

    public MemberCoupons findByIds(List<Long> ids) {
        return new MemberCoupons(memberCouponDao.findByIds(ids));
    }

    public void updateCoupons(MemberCoupons memberCoupons, Member member) {
        memberCouponDao.updateCoupon(memberCoupons, member.getId());
    }

    public Long create(Long memberId, Long couponId) {
        return memberCouponDao.create(memberId, couponId);
    }
}
