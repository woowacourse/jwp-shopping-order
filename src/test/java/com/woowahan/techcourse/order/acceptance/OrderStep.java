package com.woowahan.techcourse.order.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequestDto;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequestDto.CreateOrderCartItem;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequestDto.CreateOrderCartItem.CreateOrderProduct;
import com.woowahan.techcourse.order.ui.dto.response.OrdersResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderStep {

    public static ExtractableResponse<Response> 주문_생성_요청1(String email, String password) {
        var request = new CreateOrderRequestDto(List.of(
                new CreateOrderCartItem(7L, 5,
                        new CreateOrderProduct(1L, 10000, "치킨", "http://example.com/chicken.jpg")),
                new CreateOrderCartItem(8L, 2,
                        new CreateOrderProduct(2L, 24000, "피자", "http://example.com/pizza.jpg"))
        ), List.of(1L));
        return RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(email, password)
                .when()
                .post("/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_전체_조회_요청(String email, String password) {
        return RestAssured.given()
                .auth().preemptive().basic(email, password)
                .when()
                .get("/orders")
                .then().log().all()
                .extract();
    }

    public static OrdersResponse 주문_전체_조회_결과_추출(ExtractableResponse<Response> 주문_전체_조회_결과) {
        return 주문_전체_조회_결과.as(OrdersResponse.class);
    }

    public static ExtractableResponse<Response> 주문_조회_ID로_요청(Long orderId, String email, String password) {
        return RestAssured.given()
                .auth().basic(email, password)
                .when()
                .get("/orders/" + orderId)
                .then().log().all()
                .extract();
    }

    public static Long 주문_생성_결과에서_주문_아이디_추출(ExtractableResponse<Response> 주문_생성_결과) {
        String location = 주문_생성_결과.header("Location").split("/")[2];
        return Long.parseLong(location);
    }

    public static void 전체_조회_응답의_사이즈는_N이다(OrdersResponse 주문_전체_조회_결과, int size) {
        assertThat(주문_전체_조회_결과.getOrders()).hasSize(size);
    }
}
