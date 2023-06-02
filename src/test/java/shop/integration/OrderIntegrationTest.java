package shop.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shop.application.member.dto.MemberLoginDto;
import shop.web.controller.order.dto.request.OrderCreationRequest;
import shop.web.controller.order.dto.request.OrderItemRequest;

import java.util.List;

public class OrderIntegrationTest extends IntegrationTest {

    @DisplayName("주문을 할 수 있다.")
    @Test
    void orderTest() {
        //given
        MemberLoginDto memberLoginDto = new MemberLoginDto("a@a.com", "1234");

        String token = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(memberLoginDto)
                .when().post("/users/login")
                .then().log().all()
                .extract().jsonPath().getString("token");

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
}
