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

    public static long 주문_등록하고_아이디_반환(final Member member, final OrderRequest request) {
        final ExtractableResponse<Response> response = 주문_등록_요청(member, request);
        return 응답에서_주문_아이디_추출(response);
    }

    public static long 응답에서_주문_아이디_추출(final ExtractableResponse<Response> 주문_등록_응답) {
        final String location = 주문_등록_응답.header("location");
        return Long.parseLong(location.split("/")[2]);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청(final Member member) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())

                .when()
                .get("/orders")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상세_조회_요청(final Member member, final long orderId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())

                .when()
                .get("/orders/" + orderId)

                .then()
                .extract();
    }
}
