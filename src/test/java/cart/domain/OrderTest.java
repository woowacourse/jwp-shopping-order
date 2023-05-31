package cart.domain;

import cart.exception.PointException;
import cart.exception.PriceInconsistencyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.ShoppingOrderFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OrderTest {

    // originalPrice = 79000, pointToAdd = 6900
    private static final List<CartItem> cartItems = List.of(
            new CartItem(1L, 2, chicken, member1),
            new CartItem(2L, 1, salad, member1),
            new CartItem(3L, 3, pizza, member1));

    @DisplayName("올바른 주문이 요청되면 order 도메인은 프론트에서 보낸 originalPrice와 동일한 값을 가진다")
    @Test
    void validateOriginalPrice() {
        Order order = new Order(member1, cartItems, 79000L, 0L, 6900L);

        assertThat(order.getOriginalPrice()).isEqualTo(79000);
    }

    @DisplayName("요청된 주문의 originalPrice와 실제 주문된 Product의 값의 합이 다르면 예외를 반환한다")
    @Test
    void throwExceptionWithWrongOriginalPrice() {
        assertThatThrownBy(() -> new Order(member1, cartItems, 80000L, 0L, 6900L))
                .isInstanceOf(PriceInconsistencyException.class);
    }

    @DisplayName("요청된 주문의 pointToAdd와 실제 제공되는 포인트가 다를 경우 예외를 반환한다")
    @Test
    void throwExceptionWithWrongPointToAdd() {
        assertThatThrownBy(() -> new Order(member1, cartItems, 79000L, 0L, 6500L))
                .isInstanceOf(PointException.PointInconsistencyException.class);
    }

    @DisplayName("요청된 주문의 usedPoint가 실제 member가 가진 포인트보다 크면 예외를 반환한다")
    @Test
    void throwExceptionWhenUsedPointBiggerThanMemberPoint() {
        assertThatThrownBy(() -> new Order(member1, cartItems, 79000L, 12000L, 6900L))
                .isInstanceOf(PointException.InvalidPointException.class);
    }

    @DisplayName("요청된 주문의 usedPoint가 실제 주문 품목에 사용 가능한 포인트보다 크면 예외를 반환한다")
    @Test
    void throwExceptionWhenUsedPointBiggerThanCanUsePoint() {
        assertThatThrownBy(() -> new Order(member2, cartItems, 79000L, 60000L, 6900L))
                .isInstanceOf(PointException.InvalidPointException.class);
    }
}
