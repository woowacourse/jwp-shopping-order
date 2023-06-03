package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.MemberCoupon;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public List<MemberCoupon> findAllByMemberId(Long memberId) {
        return memberCouponDao.findUnusedByMemberId(memberId);
    }

    public List<MemberCoupon> findAllByMemberCouponIds(List<Long> couponIds) {
        return memberCouponDao.findAllByIds(couponIds);
    }

    public void useCoupons(List<MemberCoupon> memberCoupons) {
        List<Long> couponIds = memberCoupons.stream()
                .map(MemberCoupon::getId)
                .collect(Collectors.toList());
        memberCouponDao.uses(couponIds);
    }
}
