package cart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderDetailTest {

    @Test
    void 수량이_0_미만이면_예외발생() {
        assertThatThrownBy(
                () -> new OrderDetail(new Product("치킨",5000,"image"), -5L)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("수량은 최소 0개여야 합니다");
    }

}