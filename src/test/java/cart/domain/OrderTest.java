package cart.domain;

import cart.Fixture;
import cart.exception.OrderException;
import cart.exception.PointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    @DisplayName("주문을 생성한다")
    @Test
    void generate() {
        final Order actual = Order.of(Fixture.memberA, 0, List.of(Fixture.cartItem1));

        assertAll(
                () -> assertThat(actual.getMemberId()).isEqualTo(Fixture.memberA.getId()),
                () -> assertThat(actual.getOrderItems().size()).isEqualTo(1),
                () -> assertThat(actual.getTotalPrice()).isEqualTo(3000),
                () -> assertThat(actual.getPayPrice()).isEqualTo(3000),
                () -> assertThat(actual.getEarnedPoints()).isEqualTo(300),
                () -> assertThat(actual.getUsedPoints()).isEqualTo(0)
        );
    }

    @DisplayName("사용 포인트가 이용자가 보유한 포인트보다 크면 예외가 발생한다")
    @Test
    void generatePointFail() {
        assertThatThrownBy(() -> Order.of(Fixture.memberA, 9999, List.of(Fixture.cartItem1)))
                .isInstanceOf(PointException.NotEnough.class);
    }

    @DisplayName("주문한 상품의 개수가 0이면 예외가 발생한다")
    @Test
    void generateCartItemSizeFail() {
        assertThatThrownBy(() -> Order.of(Fixture.memberA, 0, List.of()))
                .isInstanceOf(OrderException.EmptyOrder.class);
    }
}
