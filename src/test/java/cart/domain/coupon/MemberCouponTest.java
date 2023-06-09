package cart.domain.coupon;

import cart.domain.member.Member;
import cart.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberCouponTest {

    private Member member;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "name", "password");
        coupon = new Coupon("coupon", 10, 365, LocalDateTime.MAX);
    }

    @DisplayName("MemberCoupon이 정상적으로 생성된다.")
    @Test
    void memberCoupon() {

        // when & then
        assertDoesNotThrow(() -> new MemberCoupon(member, coupon));
    }

    @DisplayName("MemberCoupon이 생성될 때 MemberCoupon의 만료일자를 계산한다.")
    @Test
    void memberCoupon_calculateExpiredDate() {
        // given
        LocalDateTime nextDay = LocalDateTime.now().plusDays(1);
        Coupon couponExpiringTomorrow = new Coupon("coupon", 10, 365, nextDay);

        // when
        MemberCoupon memberCoupon1 = new MemberCoupon(member, coupon);
        MemberCoupon memberCoupon2 = new MemberCoupon(member, couponExpiringTomorrow);

        // then
        assertAll(
                () -> assertThat(memberCoupon1.getExpiredAt()).isEqualTo(memberCoupon1.getIssuedAt().plusDays(365)),
                () -> assertThat(memberCoupon2.getExpiredAt()).isEqualTo(nextDay)
        );
    }


    @DisplayName("MemberCoupon이 사용자의 소유인지 확인한다.")
    @Test
    void checkOwner() {
        // given
        MemberCoupon memberCoupon = new MemberCoupon(member, coupon);
        Member differentMember = new Member(2L, "a@a.com", "1234");

        // when & then
        assertAll(
                () -> assertDoesNotThrow(() -> memberCoupon.checkOwner(member)),
                () -> assertThatThrownBy(() -> memberCoupon.checkOwner(differentMember))
                        .isInstanceOf(AuthorizationException.class)
                        .extracting("errorCode")
                        .isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }
}
