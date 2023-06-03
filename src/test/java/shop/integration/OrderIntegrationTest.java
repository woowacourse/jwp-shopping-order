package shop.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.web.controller.order.dto.request.OrderCreationRequest;
import shop.web.controller.order.dto.request.OrderItemRequest;

import java.util.List;

public class OrderIntegrationTest extends IntegrationTest {
    private static final String name = "test1234";
    private static final String password = "test";


    @DisplayName("주문을 할 수 있다.")
    @Test
    void orderTest() {
        //given
        join();
        String token = login();

        List<OrderItemRequest> orderItemRequests = List.of(
                new OrderItemRequest(1L, 5),
                new OrderItemRequest(2L, 5)
        );

        OrderCreationRequest request = new OrderCreationRequest(orderItemRequests, null);

        //when then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/" + 1L);
    }

    @DisplayName("쿠폰을 적용하여 주문할 수 있다")
    @Test
    void createOrderWithCouponTest() {
        //given
        join();
        String token = login();

        List<OrderItemRequest> orderItemRequests = List.of(
                new OrderItemRequest(1L, 5),
                new OrderItemRequest(2L, 5)
        );

        OrderCreationRequest request = new OrderCreationRequest(orderItemRequests, 1L);

        //when then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/" + 1L);

    }


    private void join() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(new MemberJoinDto(name, password))
                .post("/users/join")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    //    @DisplayName("주문 기록을 조회할 수 있다")
//    @Test
//    void getAllOrderHistoryTest() {
//
//        //given
//        join();
//        String token = login();
//
//        //when
//
//
//        //then
//
//    }
    private String login() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(new MemberLoginDto(name, password))
                .post("/users/login")
                .then()
                .log().all()
                .extract();

        return response.jsonPath().getString("token");
    }
}
