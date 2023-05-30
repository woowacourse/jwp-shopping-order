package cart.acceptence.steps;

import cart.domain.Member;
import cart.dto.request.OrderRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class OrderSteps {

    public static ExtractableResponse<Response> 주문_등록_요청(final Member member, final OrderRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(request)

                .when()
                .post("/orders")

                .then()
                .extract();
    }
}
