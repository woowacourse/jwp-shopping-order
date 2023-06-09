package cart.coupon.application;

import cart.coupon.dao.MemberCouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.MemberCoupon;
import cart.coupon.exception.CouponException;
import cart.coupon.exception.CouponExceptionType;
import cart.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponCommandService {

  private final MemberCouponDao memberCouponDao;

  public CouponCommandService(final MemberCouponDao memberCouponDao) {
    this.memberCouponDao = memberCouponDao;
  }

  public void updateUsedCoupon(final Coupon coupon, final Member member) {
    final Long couponId = coupon.getId();
    final Long memberId = member.getId();

    final MemberCoupon memberCoupon = memberCouponDao.findByMemberAndCouponId(
            couponId,
            memberId)
        .orElseThrow(() -> new CouponException(CouponExceptionType.NOT_FOUND_MEMBER_COUPON));

    memberCoupon.changeUsedStatus();

    memberCouponDao.updateMemberCoupon(
        couponId,
        memberId,
        memberCoupon.getUsedStatus().getValue()
    );
  }
}
