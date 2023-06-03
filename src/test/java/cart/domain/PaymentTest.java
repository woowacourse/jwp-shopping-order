package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.member.application.Member;
import cart.member.application.Point;
import cart.order.application.Order;
import cart.order.application.OrderItem;
import cart.order.application.Payment;
import cart.product.application.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Payment 단위테스트")
class PaymentTest {

    @Test
    void 주문에_대해_결제한다() {
        // given
        Member member = new Member(1L, "a@a.com", "1234", new Point(5000));

        Product chicken = new Product(1L, "치킨", 5000, "http://chickenimageurl", 30);
        Product pizza = new Product(2L, "피자", 10000, "http://pizzaimageurl", 30);
        OrderItem chickenOrder = new OrderItem(chicken.getId(), chicken, 3);
        OrderItem pizzaOrder = new OrderItem(pizza.getId(), pizza, 5);

        Order order = Order.makeOrder(member, List.of(chickenOrder, pizzaOrder));

        // when
        Payment payment = Payment.makePayment(order, member, new Point(3000));

        // then
        assertThat(payment.getTotalProductPrice()).isEqualTo(65000);
        assertThat(payment.getTotalDeliveryFee()).isEqualTo(0);
        assertThat(payment.getUsePoint()).isEqualTo(3000);
        assertThat(payment.getTotalPrice()).isEqualTo(62000);
    }
}
