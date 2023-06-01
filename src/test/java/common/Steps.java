package common;

import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class Steps {

    public static ExtractableResponse<Response> 상품을_장바구니에_담는다(CartItemRequest cartItemRequest, Member member) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, TestUtils.toBasicAuthHeaderValue(getAuthValue(member)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TestUtils.toJson(cartItemRequest))
                .when()
                .post("/cart-items")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니의_상품을_주문한다(OrderRequest orderRequest, Member member) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, TestUtils.toBasicAuthHeaderValue(getAuthValue(member)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TestUtils.toJson(orderRequest))
                .when()
                .post("/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상세정보를_가져온다(Member member, String uri) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, TestUtils.toBasicAuthHeaderValue(getAuthValue(member)))
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private static String getAuthValue(Member member) {
        return member.getEmail() + ":" + member.getPassword();
    }
}
