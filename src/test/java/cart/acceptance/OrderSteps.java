package cart.acceptance;

import static cart.acceptance.CommonSteps.LOCATION_헤더에서_ID_추출;
import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class OrderSteps {
    public static ExtractableResponse<Response> 주문_요청(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    public static Long 주문_요청하고_아이디_반환(Member member, OrderRequest orderRequest) {
        ExtractableResponse<Response> response = 주문_요청(member, orderRequest);
        return LOCATION_헤더에서_ID_추출(response);
    }

    public static ExtractableResponse<Response> 주문_상세_조회_요청(Member member, Long orderId) {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청(Member member) {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }
}
