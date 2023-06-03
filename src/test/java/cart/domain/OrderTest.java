package cart.domain;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.OrderException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    private static final Member MEMBER = new Member(1L, "huchu@woowahan.com", "1234567a!", 1000);
    private static final Product CHICKEN = new Product(1L, "chicken", 20000, "chicken.jpeg");
    private static final Product COKE = new Product(2L, "coke", 2000, "coke.jpeg");
    private static final OrderItem CHICKEN_ORDER_ITEM = new OrderItem(CHICKEN, 1);
    private static final OrderItem COKE_ORDER_ITEM = new OrderItem(COKE, 1);

    @Test
    void 정상적으로_생성된다() {
        //given
        final List<OrderItem> orderItems = List.of(CHICKEN_ORDER_ITEM, COKE_ORDER_ITEM);

        //expect
        assertThatNoException().isThrownBy(() -> Order.from(1L, MEMBER, 21000, 1000, orderItems));
    }

    @Test
    void 주문을_생성할_때_가용_포인트를_초과한_포인트가_입력되면_예외를_던진다() {
        //given
        final List<OrderItem> orderItems = List.of(CHICKEN_ORDER_ITEM, COKE_ORDER_ITEM);

        //expect
        assertThatThrownBy(() -> Order.from(1L, MEMBER, 21000, 1001, orderItems))
                .isInstanceOf(OrderException.IllegalPoint.class)
                .hasMessage("가용 포인트를 초과했습니다.");
    }

    @Test
    void 주문을_생성할_때_총_결제_금액이_총_상품_가격과_맞지_않으면_예외를_던진다() {
        //given
        final List<OrderItem> orderItems = List.of(CHICKEN_ORDER_ITEM, COKE_ORDER_ITEM);

        //expect
        assertThatThrownBy(() -> Order.from(1L, MEMBER, 21001, 1000, orderItems))
                .isInstanceOf(OrderException.IllegalPayment.class)
                .hasMessage("총 결제 금액이 총 상품 가격과 맞지 않습니다.");
    }

    @Test
    void 회원의_포인트를_계산한다() {
        //given
        final Order order = Order.from(1L, MEMBER, 21500, 500, List.of(CHICKEN_ORDER_ITEM, COKE_ORDER_ITEM));
        final Point savePoint = Point.valueOf(1000);

        //when
        final Member member = order.calculateMemberPoint(savePoint);

        //then
        assertThat(member).isEqualTo(new Member(1L, "huchu@woowahan.com", "1234567a!", 1500));
    }
}
