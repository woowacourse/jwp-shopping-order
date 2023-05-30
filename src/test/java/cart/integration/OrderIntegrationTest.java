package cart.integration;


import cart.dto.OrderCouponResponse;
import cart.dto.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    static final OrderCouponResponse 쿠폰1 = new OrderCouponResponse(1L, "반짝할인(10%)", 10000, true, 3000);
    static final OrderCouponResponse 쿠폰2 = new OrderCouponResponse(3L, "반짝할인(20%)", 20000, false, null);

    @DisplayName("장바구니 목록에 사용가능한 쿠폰 정보를 응답한다")
    @Test
    void 장바구니_목록에_사용가능한_쿠폰_정보를_응답한다() throws JsonProcessingException {
        Response response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .param("cartItemId", 1)
                .param("cartItemId", 3)
                .when()
                .get("/orders/coupons")
                .then()
                .log().all()
                .extract().response();

        String jsonResponse = response.getBody().asString();
        List<OrderCouponResponse> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<OrderCouponResponse>>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(쿠폰1, 쿠폰2));
    }

    @DisplayName("장바구니 아이디와 쿠폰 정보를 통해 주문한다")
    @Test
    void 장바구니_아이디와_쿠폰_정보를_통해_주문한다() {
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1L);
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response();
    }
}
