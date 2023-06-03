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
import shop.domain.coupon.CouponType;
import shop.web.controller.cart.dto.CartItemRequest;
import shop.web.controller.order.dto.request.OrderCreationRequest;
import shop.web.controller.order.dto.request.OrderItemRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
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

    @DisplayName("주문 상세 조회를 할 수 있다.")
    @Test
    void getOrderDetailTest() {
        //given
        join();
        String token = login();

        List<OrderItemRequest> orderItemRequests = List.of(
                new OrderItemRequest(1L, 2),
                new OrderItemRequest(2L, 3)
        );

        OrderCreationRequest request = new OrderCreationRequest(orderItemRequests, 1L);

        //when
        String location = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        //then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .when().get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        String couponName = response.jsonPath().getString("coupon.name");
        List<Long> productIds = response.jsonPath().getList("items.product.id.flatten()", Long.class);
        List<Integer> quantities = response.jsonPath().getList("items.quantity.flatten()", Integer.class);
        long totalPrice = response.jsonPath().getLong("totalPrice");
        long couponDiscountPrice = response.jsonPath().getLong("couponDiscountPrice");
        long discountedTotalPrice = response.jsonPath().getLong("discountedTotalPrice");

        assertThat(couponName).isEqualTo(CouponType.WELCOME_JOIN.getName());
        assertThat(productIds).containsExactlyInAnyOrder(1L, 2L);
        assertThat(quantities).containsExactlyInAnyOrder(2, 3);
        assertThat(totalPrice).isEqualTo(10000 * 2 + 20000 * 3); // 치킨 10,000원, 샐러드 20,000원 -> 80,000원
        assertThat(couponDiscountPrice).isEqualTo(8000); // 80,000원 / 10% = 8,000원
        assertThat(discountedTotalPrice).isEqualTo(72000); // 80,000 - 8,000 = 72,000
    }

    @DisplayName("주문시, 해당 상품들이 장바구니에서 삭제된다")
    @Test
    void deleteCartItemsAfterOrderTest() {
        //given
        join();
        String token = login();
        addCartItem(token, 1L);
        addCartItem(token, 2L);
        addCartItem(token, 3L);

        List<OrderItemRequest> orderItemRequests = List.of(
                new OrderItemRequest(1L, 2),
                new OrderItemRequest(2L, 3)
        );

        OrderCreationRequest request = new OrderCreationRequest(orderItemRequests, 1L);

        //when
        ExtractableResponse<Response> responseBeforeOrder = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .when().get("/cart-items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Long> productIdsBeforeOrder =
                responseBeforeOrder.jsonPath().getList("product.id.flatten()", Long.class);
        assertThat(productIdsBeforeOrder).containsExactlyInAnyOrder(1L, 2L, 3L);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        //then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + token)
                .when().get("/cart-items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Long> productIds = response.jsonPath().getList("product.id.flatten()", Long.class);

        assertThat(productIds).containsExactlyInAnyOrder(3L);
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

    private void addCartItem(String userToken, Long productId) {
        CartItemRequest cartItemRequest = new CartItemRequest(productId);

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + userToken)
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all();

    }
}
