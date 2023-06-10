package cart.coupon.application;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cart.coupon.dao.MemberCouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.FixDiscountCoupon;
import cart.coupon.domain.MemberCoupon;
import cart.coupon.domain.UsedStatus;
import cart.member.domain.Member;
import cart.value_object.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponCommandServiceTest {

  @Autowired
  private CouponCommandService couponCommandService;

  @Autowired
  private MemberCouponDao memberCouponDao;

  @Test
  @DisplayName("updateUsedCoupon() : 쿠폰 사용 상태를 현재와 반대 상태로 변경할 수 있다.")
  void test_updateUsedCoupon() throws Exception {
    //given
    final long couponId = 1L;
    final long memberId = 1L;

    final Member member = new Member(memberId, "email", "password");
    final Coupon coupon = new FixDiscountCoupon(
        couponId,
        "coupon",
        new Money(5000)
    );

    final MemberCoupon memberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    final UsedStatus beforeUsedStatus = memberCoupon.getUsedStatus();

    //when
    couponCommandService.updateUsedCoupon(coupon, member);

    //then
    final MemberCoupon afterUpdateMemberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    final UsedStatus afterUsedStatus = afterUpdateMemberCoupon.getUsedStatus();

    assertNotEquals(afterUsedStatus, beforeUsedStatus);
  }
}