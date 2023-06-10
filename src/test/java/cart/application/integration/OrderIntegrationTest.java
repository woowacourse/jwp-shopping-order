package cart.application.integration;

import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.response.SpecificOrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("모든 주문을 조회할 수 있다")
    void getAllOrders() {
        // given
        ExtractableResponse<Response> response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                // when
                .when()
                .get("/orders")
                .then()
                .extract();

        OrderResponse orderResponse = response.jsonPath().getObject("[0]", OrderResponse.class);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(orderResponse.getOrderInfos().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 주문을 조회할 수 있다")
    void getSpecificOrder() {
        // given
        ExtractableResponse<Response> response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                // when
                .when()
                .get("/orders/{id}", 1)
                .then()
                .extract();

        SpecificOrderResponse orderResponse = response.as(SpecificOrderResponse.class);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(orderResponse.getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문을 발행할 수 있다")
    void issueAnOrder() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 100000L, 0L, 2000L);

        ExtractableResponse<Response> response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                // when
                .when()
                .post("/orders")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.header("Location")).isEqualTo("/orders/3");
    }
}
