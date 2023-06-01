package cart.domain;

import cart.exception.PointException;
import cart.exception.PriceInconsistencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.ShoppingOrderFixture.*;
import static cart.ShoppingOrderFixture.member1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderManagerTest {

    // originalPrice = 79000, pointToAdd = 6900
    private static final List<CartItem> cartItems = List.of(
            new CartItem(1L, 2L, chicken, member1),
            new CartItem(2L, 1L, salad, member1),
            new CartItem(3L, 3L, pizza, member1)
    );

    private OrderManager orderManager;

    @BeforeEach
    void setup() {
        orderManager = new OrderManager(cartItems);
    }

    @DisplayName("요청된 주문의 originalPrice와 실제 주문된 Product의 값의 합이 다르면 예외를 반환한다")
    @Test
    void throwExceptionWithWrongOriginalPrice() {
        Order invalidOrder = new Order(member1, 80000L, 0L, 6900L);
        assertThatThrownBy(() -> orderManager.validateOrder(invalidOrder))
                .isInstanceOf(PriceInconsistencyException.class);
    }

    @DisplayName("요청된 주문의 pointToAdd와 실제 제공되는 포인트가 다를 경우 예외를 반환한다")
    @Test
    void throwExceptionWithWrongPointToAdd() {
        Order invalidOrder = new Order(member1, 79000L, 0L, 6500L);

        assertThatThrownBy(() -> orderManager.validateOrder(invalidOrder))
                .isInstanceOf(PointException.PointInconsistencyException.class);
    }

    @DisplayName("요청된 주문의 usedPoint가 실제 member가 가진 포인트보다 크면 예외를 반환한다")
    @Test
    void throwExceptionWhenUsedPointBiggerThanMemberPoint() {
        Order invalidOrder = new Order(member1, 79000L, 60000L, 6900L);

        assertThatThrownBy(() -> orderManager.validateOrder(invalidOrder))
                .isInstanceOf(PointException.InvalidPointException.class);
    }

    @DisplayName("요청된 주문의 usedPoint가 실제 주문 품목에 사용 가능한 포인트보다 크면 예외를 반환한다")
    @Test
    void throwExceptionWhenUsedPointBiggerThanCanUsePoint() {
        Order invalidOrder = new Order(member1, 79000L, 100000L, 6900L);

        assertThatThrownBy(() -> orderManager.validateOrder(invalidOrder))
                .isInstanceOf(PointException.InvalidPointException.class);
    }
}
