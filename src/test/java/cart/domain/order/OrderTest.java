package cart.domain.order;

import static java.lang.System.currentTimeMillis;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.cart.Quantity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.exception.OrderException;
import java.sql.Timestamp;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문목록이_1개보다_작으면_예외를_발생한다() {
        // given
        assertThatThrownBy(() -> new Order(
                1L,
                new Member(1L, new Email("a@a.com"), new Password("1234")),
                new Timestamp(currentTimeMillis()),
                of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품은 최소 1개 이상이어야 합니다.");
    }

    @Test
    void 주문목록에_담긴_모든_목록의_가격을_계산한다() {
        // given
        final Quantity quantity1 = new Quantity(5);
        final Quantity quantity2 = new Quantity(10);

        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("example.com"), new Price(1000));
        final Product product2 = new Product(2L, new Name("상품2"), new ImageUrl("example.com"), new Price(100));

        final OrderItem orderItem1 = new OrderItem(1L, quantity1, product1);
        final OrderItem orderItem2 = new OrderItem(2L, quantity2, product2);

        final Order order = new Order(
                1L,
                new Member(1L, new Email("a@a.com"), new Password("1234")),
                new Timestamp(currentTimeMillis()),
                of(orderItem1, orderItem2)
        );

        // when
        final Price totalPrice = order.getTotalPrice();

        // then
        assertThat(totalPrice.price()).isEqualTo(6000);
    }

    @Test
    void 주문이_자신의_주문이_아니면_예외가_발생한다() {
        // given
        final Member orderOwner = new Member(1L, new Email("a@a.com"), new Password("1234"));

        final Quantity quantity1 = new Quantity(5);

        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("example.com"), new Price(1000));

        final OrderItem orderItem1 = new OrderItem(1L, quantity1, product1);

        final Order order = new Order(
                1L,
                orderOwner,
                new Timestamp(currentTimeMillis()),
                of(orderItem1)
        );

        // expect
        assertThatThrownBy(() -> order.checkOwner(new Member(2L, new Email("b@b.com"), new Password("1234"))))
                .isInstanceOf(OrderException.IllegalMember.class);
    }
}
