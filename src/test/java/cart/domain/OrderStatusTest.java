package cart.domain;

import cart.exception.OrderServerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @DisplayName("주문 상태에 대한 번호를 전달하면 주문 상태를 반환한다.")
    @Test
    void findOrderStatusById_success() {
        assertThat(OrderStatus.findOrderStatusById(1)).isEqualTo(OrderStatus.PENDING);
    }


    @DisplayName("주문 상태에 없는 번호를 전달하면 예외를 던진다.")
    @Test
    void findOrderStatusById_fail() {
        assertThatThrownBy(() -> OrderStatus.findOrderStatusById(6))
                .isInstanceOf(OrderServerException.class);
    }
}
