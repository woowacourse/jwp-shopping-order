package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.ActiveCouponResponse;
import cart.dto.CouponResponse;
import cart.dto.DiscountAmountResponse;
import cart.exception.BusinessException;
import cart.factory.CouponFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  @Mock
  private MemberCouponDao memberCouponDao;

  @Mock
  private CouponDao couponDao;

  @InjectMocks
  private CouponService couponService;

  @Test
  @DisplayName("멤버가 가지고 있는 쿠폰들을 찾을 수 있다.")
  void findMemberCouponsByMember() {
    //given
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

    //when
    final List<CouponResponse> actual = couponService.findMemberCouponsByMember(member);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("사용 가능한 쿠폰을 찾을 수 있다.")
  void findActiveCoupons() {
    //given
    final Member member = new Member(1L, "email", "password");
    final boolean isUsedCoupon1 = true;
    final boolean isUsedCoupon2 = false;
    final boolean isUsedCoupon3 = false;
    final Coupon coupon1 = CouponFactory.createCoupon(1L, "coupon1", 1000, 10000);
    final Coupon coupon2 = CouponFactory.createCoupon(2L, "coupon2", 2000, 20000);
    final Coupon coupon3 = CouponFactory.createCoupon(3L, "coupon3", 1200, 12000);
    final List<ActiveCouponResponse> expected = List.of(
        new ActiveCouponResponse(coupon3.getId(), coupon3.getName(), coupon3.getMinAmount().getValue()));
    final int totalAmount = 15000;
    given(memberCouponDao.findAvailableCouponsByMemberIdAndTotalAmount(member.getId(), totalAmount)).willReturn(List.of(
        new MemberCoupon(1L, member, coupon3, isUsedCoupon3)
    ));

    //when
    final List<ActiveCouponResponse> actual = couponService.findActiveCoupons(member, totalAmount);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("할인 금액을 계산할 수 있다.")
  void calculateDiscountAmount() {
    //given
    final Member member = new Member(1L, "email", "password");
    final int totalAmount = 15000;
    final int discountAmount = 1000;
    final Coupon coupon = CouponFactory.createCoupon(1L, "coupon1", discountAmount, 10000);
    given(memberCouponDao.existsByMemberIdAndCouponId(member.getId(), coupon.getId())).willReturn(true);
    given(couponDao.findById(coupon.getId())).willReturn(Optional.of(coupon));
    final DiscountAmountResponse expect = new DiscountAmountResponse(totalAmount - discountAmount,
        discountAmount);

    //when
    final DiscountAmountResponse actual = couponService.calculateDiscountAmount(member, coupon.getId(),
        totalAmount);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
  }

  @Test
  @DisplayName("해당 멤버가 가지고 있는 쿠폰이 아닌경우 에러가 발생한다.")
  void calculateDiscountAmountWithInvalidCoupon() {
    //given
    final Member member = new Member(1L, "email", "password");
    final Coupon coupon = CouponFactory.createCoupon(1L, "coupon1", 1000, 10000);
    given(memberCouponDao.existsByMemberIdAndCouponId(member.getId(), coupon.getId())).willReturn(false);

    //when
    //then
    assertThatThrownBy(() -> couponService.calculateDiscountAmount(member, coupon.getId(), 15000))
        .isInstanceOf(BusinessException.class);
  }
}
