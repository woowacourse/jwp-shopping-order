package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import cart.domain.member.MemberName;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CouponNameTest {

    @ParameterizedTest(name = "1~50글자의 이름이 들어오면 정상적으로 생성된다.")
    @ValueSource(strings = {"쿠", "안녕하세요 이건 50글자 이상의 이름을 만들기 위해 노력하는 모습입니다. 50글자 채우기!"})
    void create(final String name) {
        assertDoesNotThrow(() -> CouponName.create(name));
    }

    @ParameterizedTest(name = "1글자 미만, 50글자 초과의 이름이 들어오면 예외가 발생한다.")
    @ValueSource(strings = {"", "안녕하세요 이건 50글자 이상의 이름을 만들기 위해 노력하는 모습입니다. 50글자 채우기!!"})
    void create_fail(final String name) {
        assertThatThrownBy(() -> CouponName.create(name))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_NAME_LENGTH);
    }
}
