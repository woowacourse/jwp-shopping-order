package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.auth.AuthorizationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Order 단위테스트")
class OrderTest {

    @Test
    void order을_생성한다() {
        // given
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        Product chicken = new Product(1L, "치킨", 5000, "http://chickenimageurl", 30);
        Product pizza = new Product(2L, "피자", 10000, "http://pizzaimageurl", 30);
        OrderItem chickenOrder = new OrderItem(chicken.getId(), chicken, 3);
        OrderItem pizzaOrder = new OrderItem(pizza.getId(), pizza, 5);

        // when
        Order order = Order.makeOrder(member, List.of(chickenOrder, pizzaOrder));

        // then
        assertThat(order.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    void order의_소유자를_체크할_수_있다() {
        // given
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        Product chicken = new Product(1L, "치킨", 5000, "http://chickenimageurl", 30);
        Product pizza = new Product(2L, "피자", 10000, "http://pizzaimageurl", 30);
        OrderItem chickenOrder = new OrderItem(chicken.getId(), chicken, 3);
        OrderItem pizzaOrder = new OrderItem(pizza.getId(), pizza, 5);

        Order order = Order.makeOrder(member, List.of(chickenOrder, pizzaOrder));

        // when
        Member illegalMember = new Member(2L, "a@a.com", "1234", new Point(3000));

        // then
        assertThatThrownBy(() -> order.checkOwner(illegalMember))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("해당 member의 order이 아닙니다.");
    }

    @Test
    void order에_포함된_총_상품금액을_계산한다() {
        // given
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        Product chicken = new Product(1L, "치킨", 5000, "http://chickenimageurl", 30);
        Product pizza = new Product(2L, "피자", 10000, "http://pizzaimageurl", 30);
        OrderItem chickenOrder = new OrderItem(chicken.getId(), chicken, 3);
        OrderItem pizzaOrder = new OrderItem(pizza.getId(), pizza, 5);

        Order order = Order.makeOrder(member, List.of(chickenOrder, pizzaOrder));

        // when
        int actualPrice = order.calculateTotalProductPrice();

        // then
        assertThat(actualPrice).isEqualTo(65000);
    }
}
