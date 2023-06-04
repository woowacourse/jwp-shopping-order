package cart.member_coupon.application;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cart.member_coupon.dao.MemberCouponDao;
import cart.member_coupon.domain.MemberCoupon;
import cart.member_coupon.domain.UsedStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberCouponCommandServiceTest {

  @Autowired
  private MemberCouponCommandService memberCouponCommandService;

  @Autowired
  private MemberCouponDao memberCouponDao;

  @Test
  @DisplayName("updateUsedCoupon() : 쿠폰 사용 상태를 현재와 반대 상태로 변경할 수 있다.")
  void test_updateUsedCoupon() throws Exception {
    //given
    final long couponId = 1L;
    final long memberId = 1L;

    final MemberCoupon memberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    final UsedStatus beforeUsedStatus = memberCoupon.getUsedStatus();

    //when
    memberCouponCommandService.updateUsedCoupon(couponId, memberId);

    //then
    final MemberCoupon afterUpdateMemberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    final UsedStatus afterUsedStatus = afterUpdateMemberCoupon.getUsedStatus();

    assertNotEquals(afterUsedStatus, beforeUsedStatus);
  }
}
