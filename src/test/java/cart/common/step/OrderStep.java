package cart.common.step;

import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class OrderStep {

    public static ExtractableResponse<Response> addOrder(final String email, final String password, final OrderRequest orderRequest) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(email, password)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all().extract();
    }

    public static Long addOrderAndGetId(final String email, final String password, final OrderRequest orderRequest) {
        final ExtractableResponse<Response> response = addOrder(email, password, orderRequest);
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
