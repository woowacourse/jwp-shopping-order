package cart.application.domain;

import cart.application.exception.ExceedAvailablePointException;
import cart.application.exception.ExceedOwnedPointException;
import cart.application.exception.IllegalMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Member member;
    private OrderInfos orderInfos;

    @BeforeEach
    void setup() {
        member = new Member(0L, "", "", 7000L);
        orderInfos = new OrderInfos(List.of(
                new OrderInfo(0L, new Product(0L, "", 1000, "", 10.0, true), "", 1000, "", 4),
                new OrderInfo(0L, new Product(0L, "", 1000, "", 10.0, true), "", 1000, "", 3)
        ));
    }

    @Test
    @DisplayName("사용 가능한 포인트를 초과해서 사용하려는 경우, 예외를 던진다")
    void usePoint_exception() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 8000L, 700L);
        // when, then
        assertThatThrownBy(() -> order.usePoint())
                .isInstanceOf(ExceedAvailablePointException.class);
    }

    @Test
    @DisplayName("사용 가능한 포인트를 초과하지 않는 경우 멤버의 포인트를 차감한다")
    void usePoint() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 7000L, 700L);
        // when
        order.usePoint();
        // then
        assertThat(order.getMember().getPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("총 적립될 포인트를 계산할 수 있다")
    void earnPoint() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 7000L, 700L);
        // when
        order.earnPoint();
        // then
        assertThat(order.getMember().getPoint()).isEqualTo(7700L);
    }

    @Test
    @DisplayName("최종 포인트를 계산할 수 있다")
    void adjustPoint() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 7000L, 700L);
        // when
        order.adjustPoint();
        // then
        assertThat(order.getMember().getPoint()).isEqualTo(700L);
    }

    @Test
    @DisplayName("주문자가 아니라면 예외를 던진다")
    void validateIsIssuedBy_exception() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 7000L, 700L);
        Member another = new Member(1L, "", "", 3000L);
        // when, then
        assertThatThrownBy(() -> order.validateIsIssuedBy(another))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    @DisplayName("주문자라면 예외를 던지지 않는다")
    void validateIsIssuedBy() {
        // given
        Order order = new Order(0L, member, orderInfos, 7000L, 7000L, 700L);
        // when, then
        assertThatCode(() -> order.validateIsIssuedBy(member))
                .doesNotThrowAnyException();
    }
}
