package cart.member_coupon.application;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.member_coupon.dao.MemberCouponDao;
import cart.member_coupon.domain.MemberCoupon;
import cart.member_coupon.domain.UsedStatus;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberCouponQueryService {

  private final MemberCouponDao memberCouponDao;

  public MemberCouponQueryService(final MemberCouponDao memberCouponDao) {
    this.memberCouponDao = memberCouponDao;
  }

  public List<Coupon> searchCouponsOwnedByMember(final Member member) {
    final List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(
        member.getId(),
        UsedStatus.UNUSED.getValue()
    );

    return memberCoupons.stream()
        .map(MemberCoupon::getCoupon)
        .collect(Collectors.toList());
  }
}
