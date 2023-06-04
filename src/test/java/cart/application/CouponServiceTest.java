package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.AvailableCouponResponse;
import cart.dto.CouponResponse;
import cart.factory.CouponFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  @Mock
  private MemberCouponDao memberCouponDao;

  @InjectMocks
  private CouponService couponService;
  @Test
  void findMemberCouponsByMemberId() {
    final Member member = new Member(1L, "email", "password");
    final boolean isUsedCoupon1 = false;
    final boolean isUsedCoupon2 = true;
    final Coupon coupon1 = CouponFactory.createCoupon(1L, "coupon1", 1000, 10000);
    final Coupon coupon2 = CouponFactory.createCoupon(2L, "coupon2", 2000, 20000);
    final List<CouponResponse> expected = List.of(
        new CouponResponse(coupon1.getId(), coupon1.getName(), coupon1.getMinAmount().getValue(),
            coupon1.getDiscountAmount().getValue(), isUsedCoupon1),
        new CouponResponse(coupon2.getId(), coupon2.getName(), coupon2.getMinAmount().getValue(),
            coupon2.getDiscountAmount().getValue(), isUsedCoupon2));
    given(memberCouponDao.findMemberCouponsByMemberId(member.getId())).willReturn(List.of(
        new MemberCoupon(1L, member, coupon1, isUsedCoupon1),
        new MemberCoupon(2L, member, coupon2, isUsedCoupon2)
    ));

    final List<CouponResponse> actual = couponService.findMemberCouponsByMemberId(member);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void findAvailableCoupons() {
    final Member member = new Member(1L, "email", "password");
    final boolean isUsedCoupon1 = true;
    final boolean isUsedCoupon2 = false;
    final boolean isUsedCoupon3 = false;
    final Coupon coupon1 = CouponFactory.createCoupon(1L, "coupon1", 1000, 10000);
    final Coupon coupon2 = CouponFactory.createCoupon(2L, "coupon2", 2000, 20000);
    final Coupon coupon3 = CouponFactory.createCoupon(3L, "coupon3", 1200, 12000);
    final List<AvailableCouponResponse> expected = List.of(
        new AvailableCouponResponse(coupon3.getId(), coupon3.getName(), coupon3.getMinAmount().getValue()));
    final int totalAmount = 15000;
    given(memberCouponDao.findAvailableCouponsByMemberIdAndTotalAmount(member.getId(), totalAmount)).willReturn(List.of(
        new MemberCoupon(1L, member, coupon3, isUsedCoupon3)
    ));

    final List<AvailableCouponResponse> actual = couponService.findAvailableCoupons(member, totalAmount);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
