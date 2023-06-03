package cart.cartitem.integration;

import cart.cartitem.ui.request.CartItemQuantityUpdateRequest;
import cart.cartitem.ui.request.CartItemRequest;
import cart.config.IntegrationTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cart.fixtures.MemberFixtures.Member_Ber;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class CartItemApiControllerTest extends IntegrationTest {

    @Test
    void 장바구니_목록을_조회하다() {
        // given, when
        final ExtractableResponse<Response> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(Member_Ber.EMAIL, Member_Ber.PASSWORD)
                .when()
                .get("/cart-items")
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 장바구니_상품을_추가하다() {
        // given
        final CartItemRequest request = CartItemRequest.from(1L);

        // when
        final ExtractableResponse<Response> result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(Member_Ber.EMAIL, Member_Ber.PASSWORD)
                .body(request)
                .when()
                .post("/cart-items")
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 장바구니_상품의_수량을_수정하다() {
        // given
        final CartItemQuantityUpdateRequest request = CartItemQuantityUpdateRequest.from(10);

        // when
        final ExtractableResponse<Response> result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(Member_Ber.EMAIL, Member_Ber.PASSWORD)
                .body(request)
                .when()
                .patch("/cart-items/" + 3L)
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 장바구니_상품을_삭제하다() {
        // given, when
        final ExtractableResponse<Response> result = given()
                .auth().preemptive().basic(Member_Ber.EMAIL, Member_Ber.PASSWORD)
                .when()
                .delete("/cart-items/" + 3L)
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
