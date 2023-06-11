package cart.integration.step;

import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.dto.request.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class OrderStep {
    public static ExtractableResponse<Response> 주문을_추가한다(Member member, OrderRequest orderRequest) {
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

    public static ExtractableResponse<Response> 주문_상세_조회한다(Member member, Long orderId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사용자별_주문을_조회한다(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    public static long 응답에서_아이디를_가져온다(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
