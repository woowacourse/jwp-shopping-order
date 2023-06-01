package cart.integration;

import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderCalculator;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static common.Fixtures.*;
import static common.Steps.상품을_장바구니에_담는다;
import static common.Steps.장바구니의_상품을_주문한다;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    OrderCalculator orderCalculator = new OrderCalculator();

    @BeforeEach
    void setUp() {
        super.setUp();
        databaseSetting.clearDatabase();
        databaseSetting.addMembers();
        databaseSetting.addProducts();
    }

    @Test
    void 사용자는_장바구니에_담긴_제품들을_주문할수있다() {
        // given
        상품을_장바구니에_담는다(A_샐러드_1);
        상품을_장바구니에_담는다(A_치킨_1);
        final List<Long> cartItems = List.of(A_샐러드_1.getId());
        final long price = (long) (A_샐러드_1.getProduct().getPrice() * A_샐러드_1.getQuantity());

        // when
        final ExtractableResponse<Response> response = 장바구니의_상품을_주문한다(주문_A_치킨_1, price);

        // then
        // TODO: 2023/06/01 주문이 db에 저장이 잘 되어 있는지, 장바구니 db에서 해당 item이 없는지, 응답으로 온 Location으로 자원을 요청할 수 있는지, 상태코드가 201인지
    }

    @Test
    void 장바구니에_없는_제품을_주문하면_404를_응답한다() {
        // given
        상품을_장바구니에_담는다(A_샐러드_1);
        final List<CartItem> cartItems = List.of(A_치킨_1);
        final Order order = Order.of(멤버_A, cartItems);

        // when
        final ExtractableResponse<Response> response = 장바구니의_상품을_주문한다(order, 치킨.getPrice());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        // TODO: 2023/06/01 주문이 db에 없는지
    }

    @Test
    void 총_상품_수량이_100개_이상이면_400을_응답한다() {
        // given
        final CartItem cartItem = new CartItem(100, 멤버_A, 치킨);
        상품을_장바구니에_담는다(cartItem);
        final Order order = Order.of(멤버_A, List.of(cartItem));
        final long paymentAmount = orderCalculator.calculatePaymentAmount(order);

        // when
        final ExtractableResponse<Response> response = 장바구니의_상품을_주문한다(order, paymentAmount);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        // TODO: 2023/06/01 주문이 db에 없는지
    }
}
