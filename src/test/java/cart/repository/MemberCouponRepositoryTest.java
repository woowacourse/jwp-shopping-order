package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("사용자의 유효한 쿠폰을 조회한다.")
    void findAvailableCouponByMember() {
        Member member = new Member(1L, "a@a", "123");
        Long orderId = orderRepository.saveOrder(new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))),
                couponRepository.findAllCoupons().get(0)));
        Coupon publishBonusCoupon = memberCouponRepository.publishBonusCoupon(orderId, member);
        Coupon coupon = memberCouponRepository.findAvailableCouponByMember(member, publishBonusCoupon.getId());

        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("주문확정_1000원_할인_보너스쿠폰"),
                () -> assertThat(coupon.getCouponTypes().getCouponTypeName()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(1000)
        );
    }

    @Test
    @DisplayName("사용자의 쿠폰을 사용완료 상태로 바꾼다.")
    void changeUserUsedCouponAvailability() {
        Member member = new Member(1L, "a@a", "123");
        Long couponId = couponRepository.publishUserCoupon(member, 1L);
        Coupon coupon = memberCouponRepository.findAvailableCouponByMember(member, couponId);

        memberCouponRepository.changeUserUsedCouponAvailability(coupon);

        assertThat(memberCouponRepository.findAvailableCouponByMember(member, couponId)).usingRecursiveComparison().isEqualTo(Coupon.EMPTY);
    }

    @Test
    @DisplayName("사용자가 보유중인 쿠폰을 조회한다.")
    void findMemberCoupons() {
        Member member = new Member(1L, "a@a", "123");
        Long couponId = couponRepository.publishUserCoupon(member, 1L);

        List<Coupon> coupons = memberCouponRepository.findMemberCoupons(member);

        assertAll(
                () -> assertThat(coupons.get(0).getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupons.get(0).getCouponTypes().getCouponTypeName()).isEqualTo("deduction"),
                () -> assertThat(coupons.get(0).getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자의 쿠폰을 사용이전 상태로 바꾼다.")
    void changeUserUnUsedCouponAvailability() {
        Member member = new Member(1L, "a@a", "123");
        Long couponId = couponRepository.publishUserCoupon(member, 1L);
        Coupon coupon = memberCouponRepository.findAvailableCouponByMember(member, couponId);

        memberCouponRepository.changeUserUsedCouponAvailability(coupon);
        memberCouponRepository.changeUserUnUsedCouponAvailability(member, couponId);
        Coupon memberCoupon = memberCouponRepository.findAvailableCouponByMember(member, couponId);
        assertAll(
                () -> assertThat(memberCoupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(memberCoupon.getCouponTypes().getCouponTypeName()).isEqualTo("deduction"),
                () -> assertThat(memberCoupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자에게 보너스 쿠폰을 지급한다.")
    void publishBonusCoupon() {
        Member member = new Member(1L, "a@a", "123");
        Long orderId = orderRepository.saveOrder(new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))),
                couponRepository.findAllCoupons().get(0)));

        Coupon coupon = memberCouponRepository.publishBonusCoupon(orderId, member);
        Coupon memberCoupon = memberCouponRepository.findAvailableCouponByMember(member, coupon.getId());

        assertAll(
                () -> assertThat(memberCoupon.getName()).isEqualTo("주문확정_1000원_할인_보너스쿠폰"),
                () -> assertThat(memberCoupon.getCouponTypes().getCouponTypeName()).isEqualTo("deduction"),
                () -> assertThat(memberCoupon.getDiscountPrice()).isEqualTo(1000)
        );
    }
}
