package cart.acceptance;

import cart.dto.CartItemCreatedResponse;
import cart.dto.CartItemRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CartItemsAcceptanceTest extends AcceptanceTest {

    @Test
    void 장바구니_아이템_추가() {
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);

        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .auth().preemptive().basic("a@a.com", "1234")
                .when().post("/cart-items")
                .then().log().all()
                .extract();

        final CartItemCreatedResponse cartItemCreatedResponse = response.as(CartItemCreatedResponse.class);

        assertAll(
                () -> assertThat(cartItemCreatedResponse.getQuantity()).isEqualTo(1),
                () -> assertThat(cartItemCreatedResponse.isChecked()).isTrue()
        );
    }
}
