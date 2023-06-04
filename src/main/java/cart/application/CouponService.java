package cart.application;

import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.dto.coupon.CouponRequest;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponDao memberCouponDao;

    public CouponService(final CouponRepository couponRepository, final MemberCouponDao memberCouponDao) {
        this.couponRepository = couponRepository;
        this.memberCouponDao = memberCouponDao;
    }

    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public Long create(CouponRequest request) {
        return couponRepository.create(request.toCoupon());
    }

    public void delete(Long id) {
        couponRepository.delete(id);
    }

    public List<MemberCoupon> findUnUsedMemberCouponByMember(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(member.getId());
        return memberCoupons.stream()
                .filter(MemberCoupon::isUnUsed)
                .collect(Collectors.toList());
    }

    public Long createMemberCoupons(Member member, Long couponId) {
        return memberCouponDao.create(member.getId(), couponId);
    }
}
