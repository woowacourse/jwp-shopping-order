package cart.order;

import cart.config.IntegrationTest;
import cart.order.ui.request.OrderCartItemRequest;
import cart.order.ui.request.OrderCartItemsRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Test
    void 장바구니에_있는_상품들을_주문한다() {
        final OrderCartItemRequest orderCartItemDto = new OrderCartItemRequest(1L, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);
        final OrderCartItemsRequest request = new OrderCartItemsRequest(List.of(orderCartItemDto));

        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(request)
                .when()
                .post("/orders")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 주문_목록을_확인하다() {
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 주문_상세를_확인하다() {
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", 1L)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
