package cart.domain.discountpolicy;

import cart.exception.OverFullPointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZlzonStoreDiscountPolicyTest {

    CouponType percentCoupon;
    CouponType amountCoupon;
    DiscountPolicy discountPolicy;

    @BeforeEach
    void setUp() {
        percentCoupon = new PercentCoupon(1000, 20);
        amountCoupon = new AmountCoupon(10000, 1500);
        discountPolicy = new ZlzonStoreDiscountPolicy();
    }

    @Test
    @DisplayName("쿠폰과 포인트를 이용해 최종 결제금액을 구한다")
    void discountTest() {
        int totalPrice = 20000;
        int usedPoint = 3000;
        int paymentPrice = discountPolicy.discount(totalPrice, List.of(percentCoupon), usedPoint);
        int expectedPaymentPrice = 13000;

        assertThat(paymentPrice).isEqualTo(expectedPaymentPrice);
    }

    @Test
    @DisplayName("쿠폰 적용 이후 남은 결제 금액이 point사용보다 많으면 예외발생")
    void discountOverPointTest() {
        int totalPrice = 11500;
        int usedPoint = 10001;
        assertThatThrownBy(() -> discountPolicy.discount(totalPrice, List.of(amountCoupon), usedPoint))
                .isInstanceOf(OverFullPointException.class)
                .hasMessage("사용하려는 포인트가 결제 예상 금액보다 큽니다.");
    }
    
}
