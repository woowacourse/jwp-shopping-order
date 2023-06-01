package cart.acceptance;

import static cart.acceptance.CommonSteps.LOCATION_헤더에서_ID_추출;
import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemSteps {

    public static ExtractableResponse<Response> 장바구니에_상품_추가_요청(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static Long 장바구니에_상품_추가하고_아이디_반환(Member member, CartItemRequest cartItemRequest) {
        ExtractableResponse<Response> response = 장바구니에_상품_추가_요청(member, cartItemRequest);
        return LOCATION_헤더에서_ID_추출(response);
    }

    public static ExtractableResponse<Response> 장바구니_상품_수량_수정_요청(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니에서_상품_삭제_요청(Member member, Long cartItemId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니에_담은_모든_상품_조회_요청(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }
}
