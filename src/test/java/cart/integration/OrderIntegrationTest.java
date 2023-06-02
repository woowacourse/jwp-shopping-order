package cart.integration;

import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static common.Fixtures.*;
import static common.Steps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderIntegrationTest extends IntegrationTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        databaseSetting.createTables();
        databaseSetting.addMembers();
        databaseSetting.addProducts();
    }

    @AfterEach
    void clear() {
        databaseSetting.clearDatabase();
    }

    @Test
    void 사용자는_장바구니에_담긴_제품들을_주문할수있다() {
        // given
        상품을_장바구니에_담는다(A_치킨_1.요청, 멤버_A);
        상품을_장바구니에_담는다(A_샐러드_1.요청, 멤버_A);
        final List<Long> cartItems = List.of(1L);
        final long price = (long) (A_치킨_1.객체.getProduct().getPrice() * A_치킨_1.객체.getQuantity());

        // when
        final ExtractableResponse<Response> 상품_주문_응답 = 장바구니의_상품을_주문한다(new OrderRequest(cartItems, price), 멤버_A);

        // then
        final ExtractableResponse<Response> 주문_상세_정보 = 주문_상세정보를_가져온다(멤버_A, 상품_주문_응답.header(HttpHeaders.LOCATION));
        assertAll(
                () -> assertThat(상품_주문_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(주문_상세_정보.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(cartItemDao.findById(1L)).isNull()
        );
    }

    @Test
    void 주문목록이_비어있으면_400를_응답한다() {
        // given
        상품을_장바구니에_담는다(A_치킨_1.요청, 멤버_A);

        // when
        final ExtractableResponse<Response> response = 장바구니의_상품을_주문한다(new OrderRequest(Collections.emptyList(), 0L), 멤버_A);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        assertThat(orderDao.findByMember(멤버_A)).isEmpty();
    }

    @Test
    void 금액이_일치하지_않으면_400을_응답한다() {
        // given
        상품을_장바구니에_담는다(A_치킨_1.요청, 멤버_A);

        // when
        final ExtractableResponse<Response> response = 장바구니의_상품을_주문한다(new OrderRequest(List.of(1L), 100L), 멤버_A);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        assertThat(orderDao.findByMember(멤버_A)).isEmpty();
    }

    @Test
    void 주문_상세_정보를_가져온다() {
        // given
        상품을_장바구니에_담는다(A_치킨_1.요청, 멤버_A);
        final List<Long> cartItems = List.of(1L);
        final long price = (long) (A_치킨_1.객체.getProduct().getPrice() * A_치킨_1.객체.getQuantity());
        final ExtractableResponse<Response> 상품_주문_응답 = 장바구니의_상품을_주문한다(new OrderRequest(cartItems, price), 멤버_A);

        // when
        final ExtractableResponse<Response> 주문_상세_정보 = 주문_상세정보를_가져온다(멤버_A, 상품_주문_응답.header(HttpHeaders.LOCATION));
        final OrderResponse orderResponse = 주문_상세_정보.as(OrderResponse.class);

        // then
        final List<OrderResponse.Item> orderItems = orderResponse.getOrderItems();
        assertAll(
                () -> assertThat(주문_상세_정보.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderItems.size()).isEqualTo(1),
                () -> assertThat(orderItems.get(0).getName()).isEqualTo(치킨.getName())
        );
    }

    @Test
    void 주문_정보가_없으면_404를_응답한다() {
        // given

        // when
        final ExtractableResponse<Response> 주문_상세_정보 = 주문_상세정보를_가져온다(멤버_A, "/orders/0");

        // then
        assertThat(주문_상세_정보.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
