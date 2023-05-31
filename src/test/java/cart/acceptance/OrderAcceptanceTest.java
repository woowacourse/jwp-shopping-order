package cart.acceptance;

import cart.dto.request.CartItemRequest;
import cart.dto.response.CartPointsResponse;
import cart.dto.request.OrderCreateRequest;
import cart.dto.response.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문을_생성한다() {
        final OrderCreateRequest request = new OrderCreateRequest(100, List.of(new CartItemRequest(1L, 1L, 2),
                new CartItemRequest(2L, 2L, 4)));

        RestAssured.given()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    @Test
    void 단일_주문을_조회한다() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when()
                .get("/orders/1")
                .then().log().all()
                .extract();

        final OrderResponse orderResponse = response.as(OrderResponse.class);

        assertAll(
                () -> assertThat(orderResponse.getCartItems().get(0).getName()).isEqualTo("치킨"),
                () -> assertThat(orderResponse.getCartItems().get(0).getPrice()).isEqualTo(10000),
                () -> assertThat(orderResponse.getCartItems().get(0).getQuantity()).isEqualTo(1),
                () -> assertThat(orderResponse.getPoints()).isEqualTo(1000),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 전체_주문을_조회한다() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when()
                .get("/orders")
                .then().log().all()
                .extract();

        final List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);

        System.out.println(orderResponses);

        assertAll(
                () -> assertThat(orderResponses).hasSize(1),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 현재_장바구니에서_얻을_수_있는_포인트_조회() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when()
                .get("/cart-points")
                .then().log().all()
                .extract();

        final CartPointsResponse cartPointsResponse = response.as(CartPointsResponse.class);

        assertAll(
                () -> assertThat(cartPointsResponse.getPoints()).isEqualTo(10000),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }
}
