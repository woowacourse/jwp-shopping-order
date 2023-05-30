package cart.steps;

import cart.domain.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

public class CartItemSteps {

    public static ExtractableResponse<Response> 카트에_아이템_추가_요청(final Member member, final CartItemRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .post("/cart-items")

                .then()
                .log().all()
                .extract();
    }

    public static Long 추가된_카트_아이템_아이디_반환(final ExtractableResponse<Response> response) {
        String locationHeader = response.header("Location");
        return Long.parseLong(locationHeader.replace("/cart-items/", ""));
    }

    public static Long 카트에_아이템_추가하고_아이디_반환(final Member member, final CartItemRequest request) {
        return 추가된_카트_아이템_아이디_반환(카트에_아이템_추가_요청(member, request));
    }

    public static ExtractableResponse<Response> 카트에_저장된_아이템의_개수_변경_요청(
            final Member member,
            final Long cartItemId,
            final CartItemQuantityUpdateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .patch("/cart-items/" + cartItemId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 카트에_저장된_아이템_삭제_요청(final Member member, final Long cartItemId) {
        return RestAssured.given()
                .log().all()
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .delete("/cart-items/" + cartItemId)

                .then()
                .extract();
    }
}
