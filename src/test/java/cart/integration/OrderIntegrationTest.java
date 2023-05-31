package cart.integration;


import cart.dto.*;
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

    static final OrderCouponResponse 쿠폰1 = new OrderCouponResponse(1L, "테스트쿠폰1", 10000, true, 3000);
    static final OrderCouponResponse 쿠폰2 = new OrderCouponResponse(2L, "테스트쿠폰2", 15000, true, 2000);

    @DisplayName("장바구니 목록에 사용가능한 쿠폰 정보를 응답한다")
    @Test
    void 장바구니_목록에_사용가능한_쿠폰_정보를_응답한다() throws JsonProcessingException {
        Response response = given().log().all()
                .auth().preemptive().basic("a@a.com", "1234")
                .param("cartItemId", 1)
                .param("cartItemId", 2)
                .when()
                .get("/orders/coupons")
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

    @DisplayName("모든 주문 정보를 조회한다")
    @Test
    void 모든_주문_정보를_조회한다() throws JsonProcessingException {
        AllOrderResponse expected = new AllOrderResponse(
                List.of(
                        new OrderResponse(
                                1L,
                                List.of(
                                        new OrderItemResponse(
                                                1L,
                                                "지구별",
                                                1000,
                                                "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg",
                                                2
                                        ),
                                        new OrderItemResponse(
                                                2L,
                                                "화성",
                                                200000,
                                                "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg",
                                                4
                                        )
                                )
                        )
                )
        );
        String jsonResponse = given().log().all()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/orders")
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        AllOrderResponse allOrderResponse = objectMapper.readValue(jsonResponse, AllOrderResponse.class);
        assertThat(allOrderResponse).usingRecursiveComparison().isEqualTo(expected);
    }
}
