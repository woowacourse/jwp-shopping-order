package cart.member_coupon.application;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.member_coupon.dao.MemberCouponDao;
import cart.member_coupon.domain.MemberCoupon;
import cart.member_coupon.domain.UsedStatus;
import cart.member_coupon.exception.NotFoundMemberCouponException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCouponCommandService {

  private final MemberCouponDao memberCouponDao;

  public MemberCouponCommandService(final MemberCouponDao memberCouponDao) {
    this.memberCouponDao = memberCouponDao;
  }

  public void updateUsedCoupon(final Coupon coupon, final Member member) {
    final Long couponId = coupon.getId();
    final Long memberId = member.getId();

    final MemberCoupon memberCoupon = memberCouponDao.findByMemberAndCouponId(
            couponId,
            memberId)
        .orElseThrow(() -> new NotFoundMemberCouponException("해당 멤버는 현재 쿠폰을 가지고 있지 않습니다."));

    final UsedStatus usedStatus = memberCoupon.getUsedStatus();

    memberCouponDao.updateMemberCoupon(couponId, memberId, usedStatus.opposite().getValue());
  }
}
