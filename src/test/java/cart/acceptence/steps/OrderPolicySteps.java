package cart.acceptence.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class OrderPolicySteps {

    public static ExtractableResponse<Response> 할인_정책_조회_요청() {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/order-policy")

                .then()
                .extract();
    }
}
