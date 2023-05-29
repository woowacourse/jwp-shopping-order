package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("쿠폰의 할인율이 0이하면 예외가 발생한다.")
    void exception() {
        Assertions.assertThatThrownBy(() -> new Coupon("쿠폰", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

}