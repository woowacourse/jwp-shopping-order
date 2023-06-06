package cart.acceptance;

import cart.dto.CartItemSaveRequest;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    void 장바구니에_담은_상품을_쿠폰을_사용하여_주문한다() {
        // given
        saveCartItem(1L);
        saveCartItem(2L);

        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1L);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 장바구니에_담은_상품을_쿠폰을_사용하지_않고_주문한다() {
        // given
        saveCartItem(1L);
        saveCartItem(2L);

        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L));

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void saveCartItem(final long productId) {
        final CartItemSaveRequest cartItemSaveRequest = new CartItemSaveRequest(productId);
        RestAssured.given()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemSaveRequest)
                .post("/cart-items");
    }

    @Test
    void 사용자의_주문_내역을_확인한다() {
        // given
        saveCartItem(1L);
        saveCartItem(2L);
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1L);
        order(orderRequest);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/orders")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<OrderResponse> orderResponses = response.jsonPath().getList("", OrderResponse.class);
        final OrderResponse orderResponse = orderResponses.get(0);
        assertThat(orderResponse.getOrderItems().size()).isEqualTo(2);
    }

    private String order(final OrderRequest orderRequest) {
        final Response post = RestAssured.given()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .post("/orders");

        return post.getHeader("Location");
    }

    @Test
    void 특정_주문을_상세_정보를_조회한다() {
        // given
        saveCartItem(1L);
        saveCartItem(2L);
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1L);
        final String location = order(orderRequest);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final OrderResponse orderResponse = response.as(OrderResponse.class);
        assertThat(orderResponse.getOrderItems().size()).isEqualTo(2);
    }
}
