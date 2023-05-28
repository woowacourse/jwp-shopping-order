package cart.acceptance;

import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문을_생성한다() {
        final OrderCreateRequest request = new OrderCreateRequest(1500, List.of(new CartItemRequest(1L, 1L, 2),
                new CartItemRequest(2L, 2L, 4)));

        RestAssured.given()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());

    }
}
