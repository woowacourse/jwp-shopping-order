package cart.acceptence.steps;

import cart.domain.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemSteps {


    public static ExtractableResponse<Response> 장바구니_아이템_추가_요청(final Member member, final long productId) {
        return 장바구니_아이템_추가_요청(member, new CartItemRequest(productId));
    }

    public static ExtractableResponse<Response> 장바구니_아이템_추가_요청(final Member member, final CartItemRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(request)

                .when()
                .post("/cart-items")

                .then()
                .extract();
    }

    public static long 장바구니_아이템_추가하고_아이디_반환(final Member member, final CartItemRequest request) {
        final ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(member, request);
        return 응답에서_장바구니_아이템_아이디_추출(response);
    }

    public static long 장바구니_아이템_추가하고_아이디_반환(final Member member, final long productId) {
        final ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(member, productId);
        return 응답에서_장바구니_아이템_아이디_추출(response);
    }

    public static long 응답에서_장바구니_아이템_아이디_추출(final ExtractableResponse<Response> 장바구니_아이템_추가_응답) {
        final String location = 장바구니_아이템_추가_응답.header("location");
        return Long.parseLong(location.split("/")[2]);
    }

    public static ExtractableResponse<Response> 장바구니_조회_요청(final Member member) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())

                .when()
                .get("/cart-items")

                .then()
                .extract();
    }

    public static Optional<CartItemResponse> 장바구니_조회하고_특정_아이템이_존재하면_반환(final Member member, final long cartItemId) {
        ExtractableResponse<Response> 장바구니_조회_결과 = 장바구니_조회_요청(member);
        return 장바구니_조회_결과.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(장바구니_아이템 -> 장바구니_아이템.getId().equals(cartItemId))
                .findFirst();
    }

    public static ExtractableResponse<Response> 장바구니_아이템_삭제_요청(final Member member, final long cartItemId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())

                .when()
                .delete("/cart-items/" + cartItemId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_아이템_수정_요청(
            final Member member,
            final long cartItemId,
            final long quantity
    ) {
        return 장바구니_아이템_수정_요청(member, cartItemId, new CartItemQuantityUpdateRequest(quantity));
    }

    public static ExtractableResponse<Response> 장바구니_아이템_수정_요청(
            final Member member,
            final long cartItemId,
            final CartItemQuantityUpdateRequest request
    ) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(request)

                .when()
                .patch("/cart-items/" + cartItemId)

                .then()
                .extract();
    }
}
