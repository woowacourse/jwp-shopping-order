package cart.integration;

import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

class OrderIntegrationTest extends IntegrationTest {

    @DisplayName("주문 기능 확인")
    @Test
    void a() {
        OrderRequest request = new OrderRequest(List.of(1L, 2L), 100000L, 0L, 10000L);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(201)
                .extract();
    }
}
