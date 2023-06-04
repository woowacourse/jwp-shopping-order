package cart.member_coupon.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import cart.coupon.domain.FixDiscountCoupon;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.member_coupon.domain.MemberCoupon;
import cart.member_coupon.domain.UsedStatus;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class MemberCouponDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private MemberCouponDao memberCouponDao;

  private MemberDao memberDao;
  private CouponDao couponDao;


  @BeforeEach
  void setUp() {
    memberDao = mock(MemberDao.class);
    couponDao = mock(CouponDao.class);

    memberCouponDao = new MemberCouponDao(jdbcTemplate, memberDao, couponDao);
  }

  @Test
  @DisplayName("findByMemberId() : member Id를 통해 member가 사용하지 않고 보유하고 있는 쿠폰들을 조회할 수 있다.")
  void test_findByMemberId() throws Exception {
    //given
    final long memberId = 1L;
    final Member member = new Member(null, null, null);
    final Coupon coupon = new EmptyCoupon();
    final String usedCondition = UsedStatus.UNUSED.getValue();

    when(memberDao.getMemberById(anyLong()))
        .thenReturn(member);

    when(couponDao.findById(anyLong()))
        .thenReturn(Optional.of(coupon));

    //when
    final List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(
        memberId,
        usedCondition
    );

    //then
    assertEquals(2, memberCoupons.size());
  }

  @Test
  @DisplayName("updateMemberCoupon() : 특정 쿠폰의 사용 상태를 변경할 수 있다.")
  void test_updateMemberCoupon() throws Exception {
    //given
    final long couponId = 2L;
    final long memberId = 3L;

    final MemberCoupon memberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    //when
    memberCouponDao.updateMemberCoupon(couponId, memberId, "Y");

    //then
    final MemberCoupon afterUpdateMemberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    ).get();

    assertNotEquals(afterUpdateMemberCoupon.getUsedStatus(),
        memberCoupon.getUsedStatus());
  }

  @Test
  @DisplayName("findByMemberAndCouponId() : 사용자가 가지고 있는 특정 쿠폰을 조회할 수 있다.")
  void test_findByMemberAndCouponId() throws Exception {
    //given
    final long memberId = 1L;
    final long couponId = 1L;
    final Member member = new Member(memberId, null, null);
    final Coupon coupon = new FixDiscountCoupon(couponId, null, null);

    when(memberDao.getMemberById(anyLong()))
        .thenReturn(member);

    when(couponDao.findById(anyLong()))
        .thenReturn(Optional.of(coupon));

    //when
    final Optional<MemberCoupon> memberCoupon = memberCouponDao.findByMemberAndCouponId(
        couponId,
        memberId
    );

    //then
    assertAll(
        () -> Assertions.assertThat(memberCoupon).isPresent(),
        () -> assertEquals(couponId, memberCoupon.get().getCoupon().getId()),
        () -> assertEquals(memberId, memberCoupon.get().getMember().getId())
    );
  }
}
