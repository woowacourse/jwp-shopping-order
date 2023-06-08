package cart.domain.coupon;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Nickname;
import cart.domain.member.Password;
import cart.exception.MemberCouponException;
import cart.exception.NoExpectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCouponTest {
    private Coupon coupon;
    private Member member;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    @BeforeEach
    public void setup() {
        CouponInfo couponInfo = new CouponInfo(1L, "금액", 3000, 1000);
        coupon = new AmountCoupon(couponInfo, 1000);
        member = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("루카"));
        expiredAt = LocalDateTime.now().plusDays(7);
        createdAt = LocalDateTime.now();
    }

    @Test
    public void 빈_쿠폰은_할인금액이_0원이다() {
        MemberCoupon memberCoupon = new EmptyMemberCoupon();
        int discount = memberCoupon.calculateDiscount(100);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    public void 사용가능한_쿠폰의_할인액을_계산한다() {
        MemberCoupon memberCoupon = new UsableMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        int discount = memberCoupon.calculateDiscount(10000);

        assertThat(discount).isEqualTo(1000);
    }

    @Test
    public void 이미_사용한_쿠폰도_할인액을_계산한다() {
        MemberCoupon memberCoupon = new UsedMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        int discount = memberCoupon.calculateDiscount(10000);

        assertThat(discount).isEqualTo(1000);
    }

    @Test
    public void 이미_사용한_쿠폰은_사용이_불가하다() {
        MemberCoupon memberCoupon = new UsedMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        assertThatThrownBy(memberCoupon::use)
                .isInstanceOf(MemberCouponException.Unavailable.class);
    }

    @Test
    public void 이미_사용한_쿠폰은_취소가_가능하다() {
        MemberCoupon memberCoupon = new UsedMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        MemberCoupon canceledCoupon = memberCoupon.cancelUsed();

        assertThat(canceledCoupon).isInstanceOf(UsableMemberCoupon.class);
    }

    @Test
    public void 사용_가능한_쿠폰은_사용이_가능하다() {
        MemberCoupon memberCoupon = new UsableMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        MemberCoupon canceledCoupon = memberCoupon.use();

        assertThat(canceledCoupon).isInstanceOf(UsedMemberCoupon.class);
    }

    @Test
    public void 사용_가능한_쿠폰은_취소가_불가하다() {
        MemberCoupon memberCoupon = new UsableMemberCoupon(1L, coupon, member, expiredAt, createdAt);

        assertThatThrownBy(memberCoupon::cancelUsed)
                .isInstanceOf(NoExpectedException.class);
    }
}
