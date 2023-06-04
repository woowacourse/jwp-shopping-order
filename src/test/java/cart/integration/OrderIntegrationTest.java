package cart.integration;


import cart.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
public class OrderIntegrationTest extends IntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("장바구니 목록에 사용가능한 쿠폰 정보를 응답한다")
    @Test
    void 장바구니_목록에_사용가능한_쿠폰_정보를_응답한다() throws JsonProcessingException {
        OrderCouponResponse 쿠폰1 = new OrderCouponResponse(
                1L,
                "테스트쿠폰1",
                10000,
                3000,
                true,
                3000,
                LocalDateTime.parse("2023-06-30 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        OrderCouponResponse 쿠폰2 = new OrderCouponResponse(
                2L,
                "테스트쿠폰2",
                15000,
                null,
                true,
                2000,
                LocalDateTime.parse("2023-06-30 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        AllOrderCouponResponse expected = new AllOrderCouponResponse(List.of(쿠폰1, 쿠폰2));

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

        // TODO: 6/1/23 찾아 보기
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        AllOrderCouponResponse actual = objectMapper.readValue(jsonResponse, AllOrderCouponResponse.class);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("장바구니 아이디와 쿠폰 정보를 통해 주문한다")
    @Test
    void 장바구니_아이디와_쿠폰_정보를_통해_주문한다() {
        OrderRequest orderRequest = new OrderRequest(
                List.of(
                        new OrderCartItemRequest(
                                1L,
                                2,
                                "지구",
                                1000,
                                "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg"
                        ),
                        new OrderCartItemRequest(
                                2L,
                                4,
                                "화성",
                                200000,
                                "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg"
                        )
                ),
                1L
        );
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
                                                "지구",
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

    @DisplayName("주문 정보를 조회한다")
    @Test
    void 주문_정보를_조회한다() throws JsonProcessingException {
        OrderDetailResponse expected = new OrderDetailResponse(
                1L,
                List.of(
                        new OrderItemResponse(
                                1L,
                                "지구",
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
                ),
                802000,
                3000,
                0
        );
        String jsonResponse = given().log().all()
                .auth().preemptive().basic("a@a.com", "1234")
                .pathParam("orderId", 1)
                .when()
                .get("/orders/{orderId}")
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        OrderDetailResponse orderDetailResponse = objectMapper.readValue(jsonResponse, OrderDetailResponse.class);
        assertThat(orderDetailResponse).usingRecursiveComparison().isEqualTo(expected);
    }
}
