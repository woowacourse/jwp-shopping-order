package cart.steps;

import cart.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

public class OrderSteps {

    public static ExtractableResponse<Response> 멤버의_주문_목록_조회_요청(final Member member) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .get("/members/orders")

                .then()
                .log().all()
                .extract();
    }
}
