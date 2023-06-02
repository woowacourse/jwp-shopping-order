package cart.integration;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderInfo;
import cart.domain.Product;
import cart.dto.CartItemRequest;
import cart.dto.OrderInfoResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.*;

class OrderIntegrationTest extends IntegrationTest {

    private static final Member testMember = new Member("a@a.com", "1234", 10000L);

    @DisplayName("상품들을 주문한다")
    @Test
    void orderItems() {
        OrderRequest request = new OrderRequest(List.of(1L, 2L), 100000L, 0L, 10000L);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .extract();

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/orders/1");
        });
    }

    @DisplayName("요청된 주문금액과 실제 금액이 다른 경우 Conflict를 반환한다")
    @Test
    void returnConflictWithWrongOriginalPrice() {
        OrderRequest request = new OrderRequest(List.of(1L, 2L), 90000L, 0L, 10000L);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("요청된 사용 포인트가 member의 보유 포인트보다 많으면 Conflict를 반환한다")
    @Test
    void returnConflictWithTooManyUsedPoint() {
        OrderRequest request = new OrderRequest(List.of(1L, 2L), 100000L, 50000L, 10000L);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("주문 상품이 장바구니 내에 없는 경우 예외를 반환한다")
    @Test
    void a() {
        Long cartItemId1 = requestAddCartItem(testMember, new CartItemRequest(1L));
        OrderRequest request1 = new OrderRequest(List.of(cartItemId1), 10000L, 0L, 1000L);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request1)
                .when().post("/orders")
                .then().log().all();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(404)
                .extract();
    }

    @DisplayName("사용자의 이전 주문 목록을 조회한다")
    @Test
    void getPastOrders() {
        Long cartItemId1 = requestAddCartItem(testMember, new CartItemRequest(1L));
        Long cartItemId2 = requestAddCartItem(testMember, new CartItemRequest(2L));
        OrderRequest request1 = new OrderRequest(List.of(cartItemId1), 10000L, 0L, 1000L);
        OrderRequest request2 = new OrderRequest(List.of(cartItemId2), 20000L, 0L, 2000L);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request1)
                .when().post("/orders")
                .then().log().all();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request2)
                .when().post("/orders")
                .then().log().all();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .when().get("/orders")
                .then().log().all()
                .statusCode(200)
                .body("", hasSize(2))
                .body("orderId", hasItems(1, 2))
                .body("originalPrice", hasItems(10000, 20000))
                .body("usedPoint", hasItem(0))
                .body("pointToAdd", hasItems(1000, 2000))
                .extract();

        List<OrderResponse> orderResponses = response.as(new TypeRef<>() {
        });

        assertThat(orderResponses).usingRecursiveComparison()
                .isEqualTo(List.of(
                                OrderResponse.of(new Order(1L, testMember, 10000L, 0L, 1000L),
                                        List.of(new OrderInfo(1L, new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"), 1L))),
                                OrderResponse.of(new Order(2L, testMember, 20000L, 0L, 2000L),
                                        List.of(new OrderInfo(1L, new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"), 1L)))
                        )
                );
    }

    @DisplayName("주문 상세정보를 조회한다")
    @Test
    void getOrderDetail() {
        Long cartItemId1 = requestAddCartItem(testMember, new CartItemRequest(1L));
        Long cartItemId2 = requestAddCartItem(testMember, new CartItemRequest(2L));
        OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), 30000L, 0L, 3000L);

        ExtractableResponse<Response> orderResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .body(request)
                .when().post("/orders")
                .then().log().all()
                .extract();
        Long orderId = getIdFromCreatedResponse(orderResponse);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(testMember.getEmail(), testMember.getPassword())
                .when().get("/orders/{orderId}", orderId)
                .then().log().all()
                .extract();

        OrderResponse resultResponse = response.as(OrderResponse.class);
        List<OrderInfoResponse> orderInfoResponse = resultResponse.getOrderInfo();

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(resultResponse.getOrderId()).isEqualTo(orderId);
            softly.assertThat(resultResponse.getOriginalPrice()).isEqualTo(30000);
            softly.assertThat(resultResponse.getUsedPoint()).isZero();
            softly.assertThat(resultResponse.getPointToAdd()).isEqualTo(3000);
            softly.assertThat(orderInfoResponse).extracting("productId", "price", "name", "quantity")
                    .containsExactly(
                            tuple(1L, 10000, "치킨", 1),
                            tuple(2L, 20000, "샐러드", 1)
                    );
        });
    }

    private Long requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when().post("/cart-items")
                .then().log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
