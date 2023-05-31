package cart.common.step;

import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class CartItemStep {

    public static ExtractableResponse<Response> addCartItem(final String email, final String password, final CartItemRequest cartItemRequest) {
        return given().log().all()
                .auth().preemptive().basic(email, password)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static Long addCartItemAndGetId(final String email, final String password, final CartItemRequest cartItemRequest) {
        final ExtractableResponse<Response> response = addCartItem(email, password, cartItemRequest);
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> updateCartItemQuantity(final String email, final String password, final Long cartId, final CartItemQuantityUpdateRequest request) {
        return given().log().all()
                .auth().preemptive().basic(email, password)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", cartId)
                .body(request)
                .when()
                .patch("/cart-items/{id}")
                .then()
                .log().all()
                .extract();
    }
}
