package cart.domain;

import cart.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderCheckoutTest {

    @DisplayName("주문 확인서 생성 테스트")
    @Test
    void generate() {
        // given
        final int currentPoints = 1000;
        final List<CartItem> cartItems = List.of(Fixture.cartItem1);

        // when
        final OrderCheckout actual = OrderCheckout.generate(currentPoints, cartItems);

        // then
        assertAll(
                () -> assertThat(actual.getCartItems()).containsExactly(Fixture.cartItem1),
                () -> assertThat(actual.getTotalPrice()).isEqualTo(3000),
                () -> assertThat(actual.getCurrentPoints()).isEqualTo(currentPoints),
                () -> assertThat(actual.getEarnedPoints()).isEqualTo(300),
                () -> assertThat(actual.getAvailablePoints()).isEqualTo(300)
        );
    }
}
