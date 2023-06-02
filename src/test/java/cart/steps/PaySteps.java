package cart.steps;

import cart.domain.Member;
import cart.dto.request.PayRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

public class PaySteps {

    public static ExtractableResponse<Response> 카트_아이템_주문_요청(final Member member, final PayRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .post("/pay")

                .then()
                .log().all()
                .extract();
    }

    public static long 카트_아이템_주문하고_주문_히스토리_아이디_반환(final Member member, final PayRequest request) {
        return 카트_아이템_주문_요청(member, request).jsonPath().getLong("orderId");
    }
}
