package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalOrderException;
import cart.exception.OrderUnauthorizedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Member member = new Member(1L, "email", "password");

    @Test
    @DisplayName("한 개 이상의 상품을 주문하지 않으면 주문에 실패한다.")
    void createOrder_empty_fail() {
        // when, then
        assertThatThrownBy(() -> new Order(Collections.emptyList(), member))
            .isInstanceOf(IllegalOrderException.class)
            .hasMessageContaining("주문 상품은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("주문할 상품의 총 금액을 계산할 수 있다.")
    void calculateTotalOrderPrice() {
        // given
        List<OrderItem> orderItems = List.of(
            new OrderItem(new Product("치킨", Money.from(10_000), "tmpImg"), Quantity.from(2)),
            new OrderItem(new Product("피자", Money.from(15_000), "tmpImg"), Quantity.from(1))
        );
        Order order = new Order(orderItems, member);

        // when
        Money price = order.calculateTotalPrice();

        // then
        Money expected = Money.from(35_000);
        assertThat(price).isEqualTo(expected);
    }

    @Test
    @DisplayName("다른 사용자의 주문 내역일 경우 예외가 발생한다.")
    void exception_otherMember() {
        // given
        List<OrderItem> orderItems = List.of(
            new OrderItem(new Product("치킨", Money.from(10_000), "tmpImg"), Quantity.from(2)),
            new OrderItem(new Product("피자", Money.from(15_000), "tmpImg"), Quantity.from(1))
        );
        Order order = new Order(1L, orderItems, Timestamp.valueOf(LocalDateTime.now()), member);

        // when, then
        assertThatThrownBy(() -> order.checkOwner(Member.fromId(2L)))
            .isInstanceOf(OrderUnauthorizedException.class)
            .hasMessageContaining("다른 사용자의 주문 내역은 조회할 수 없습니다. orderId = 1");
    }

}
