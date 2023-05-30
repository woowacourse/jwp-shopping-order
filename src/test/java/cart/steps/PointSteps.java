package cart.steps;

import cart.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PointSteps {

    public static ExtractableResponse<Response> 멤버의_포인트_조회_요청(final Member member) {
        return RestAssured.given()
                .log().all()
                .auth().preemptive()
                .basic(member.getEmail(), member.getPassword())

                .when()
                .get("/members/points")

                .then()
                .log().all()
                .extract();
    }
}
