package cart.integration;

import cart.domain.Member.Member;
import cart.domain.Point;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PointIntegrationTest extends IntegrationTest{


    private final Member member1 = new Member(1L, "a@a.com", "1234");

    @Test
    @DisplayName("사용자의 장바구니의 상품을 주문하고 포인트를 조회한다.")
    void orderCartItemAndCheckPoint(){
        // given
        long productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        long productId2 = createProduct(new ProductRequest("피자", 20_000, "http://example.com/pizza.jpg"));

        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        CartItemRequest cartItemRequest2 = new CartItemRequest(productId2);

        long cartId = requestAddCartItem(member1, cartItemRequest);
        long cartId2 = requestAddCartItem(member1, cartItemRequest2);

        createOrder(List.of(cartId, cartId2), member1, 1000);

        //
        ExtractableResponse<Response> response = showUserPoint(member1);
        assertThat(response.body().jsonPath().getInt("points")).isEqualTo(9725);
    }

    @Test
    @DisplayName("사용자의 장바구니의 상품을 주문하고 주문에 대한 포인트를 조회한다.")
    void orderCartItemAndCheckOrderPoint(){
        // given
        long productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        long productId2 = createProduct(new ProductRequest("피자", 20_000, "http://example.com/pizza.jpg"));

        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        CartItemRequest cartItemRequest2 = new CartItemRequest(productId2);

        long cartId = requestAddCartItem(member1, cartItemRequest);
        long cartId2 = requestAddCartItem(member1, cartItemRequest2);

        Long orderId = createOrder(List.of(cartId, cartId2), member1, 1000);

        //

        ExtractableResponse<Response> response = showOrderPoint(orderId, member1);
        assertThat(response.body().jsonPath().getInt("points_saved")).isEqualTo(725);
    }

    private ExtractableResponse<Response> showOrderPoint(long orderId, Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/orders/" + orderId + "/points")
                .then()
                .log().all()
                .extract();
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private Long requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private Long createOrder(List<Long> cartItemIds, Member member, int usedPoint) {
        OrderRequest orderRequest = new OrderRequest(cartItemIds, usedPoint);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> showUserPoint(Member member) {

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/points")
                .then()
                .log().all()
                .extract();
    }
}
