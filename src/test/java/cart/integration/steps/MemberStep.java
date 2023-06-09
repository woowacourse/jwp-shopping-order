package cart.integration.steps;

import static io.restassured.RestAssured.given;

import cart.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberStep {
    public static ExtractableResponse<Response> 멤버_포인트_조회_요청(Member 멤버) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .when().get("/members/point")
                .then()
                .log().all()
                .extract();
    }
}
