package shop.domain.coupon;

import shop.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CouponNameTest {
    private static final String msgOf51Length = "이것은 51 글자를 채우기 위해서 작성하고 있습니다." +
            "해당 문장은 가볍게 무시하셔도 좋습니다.";
    private static final String msgOf50Length = "이것은 50글자를 채우기 위해서 작성하고 있습니다." +
            "해당 문장은 가볍게 무시하셔도 좋습니다.";

    @ParameterizedTest(name = "쿠폰의 이름은 1글자 이상, 50글자 이하여야 한다.")
    @ValueSource(strings = {"", msgOf51Length})
    void createCouponNameTest1(String name) {
        assertThatThrownBy(() -> new CouponName(name))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "쿠폰의 이름은 1글자 이상, 50글자 이하여야 한다.")
    @ValueSource(strings = {"1", msgOf50Length})
    void createCouponNameTest2(String name) {
        assertDoesNotThrow(() -> new CouponName(name));
    }
}
