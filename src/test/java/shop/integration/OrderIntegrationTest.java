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

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("사용한 쿠폰을 재사용할 수 없다")
    @Test
    void createOrderWithUsedCouponTest() {
        //given
        join();
        String token = login();

        List<OrderItemRequest> orderItemRequests = List.of(
                new OrderItemRequest(1L, 5),
                new OrderItemRequest(2L, 5)
        );

        OrderCreationRequest request = new OrderCreationRequest(orderItemRequests, 1L);

        //when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/" + 1L);

        //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("모든 주문 내역을 조회할 수 있다")
    @Test
    void getAllOrderHistoryTest() {
        //given
        join();
        String token = login();

        List<OrderItemRequest> orderItemRequests1 = List.of(
                new OrderItemRequest(1L, 2),
                new OrderItemRequest(2L, 3)
        );

        List<OrderItemRequest> orderItemRequests2 = List.of(
                new OrderItemRequest(1L, 4),
                new OrderItemRequest(3L, 5)
        );

        OrderCreationRequest request1 = new OrderCreationRequest(orderItemRequests1, null);
        OrderCreationRequest request2 = new OrderCreationRequest(orderItemRequests2, null);

        //when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/" + 1L);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request2)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/" + 2L);

        //then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Long> orderIds = response.jsonPath().getList("orderId", Long.class);
        List<Long> productIds = response.jsonPath().getList("items.product.id.flatten()", Long.class);
        List<Integer> productQuantities = response.jsonPath().getList("items.quantity.flatten()", Integer.class);

        assertThat(orderIds).containsExactlyInAnyOrder(1L, 2L);
        assertThat(productIds).containsExactlyInAnyOrder(1L, 2L, 1L, 3L);
        assertThat(productQuantities).containsExactlyInAnyOrder(2, 3, 4, 5);
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
