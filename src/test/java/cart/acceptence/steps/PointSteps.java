package cart.acceptence.steps;

import cart.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PointSteps {

    public static ExtractableResponse<Response> 포인트_조회_요청(final Member member) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())

                .when()
                .get("/point")

                .then()
                .extract();
    }
}
