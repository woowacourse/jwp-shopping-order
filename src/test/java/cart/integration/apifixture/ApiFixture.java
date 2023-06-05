package cart.integration.apifixture;

import cart.domain.Member.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiFixture {

    public static long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    public static long addProductAndGetId(ProductRequest productRequest){
        ExtractableResponse<Response> response = createProduct(productRequest);
        return getIdFromCreatedResponse(response);
    }

    public static long addCartItemAndGetId(Member member, CartItemRequest cartItemRequest){
        ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);
        return getIdFromCreatedResponse (response);
    }

    public static long addOrderAndGetId(List<Long> cartItemIds, Member member, int usedPoint){
        ExtractableResponse<Response> response = createOrder(cartItemIds, member, usedPoint);
        return getIdFromCreatedResponse(response);
    }

    public static ExtractableResponse<Response> createProduct(ProductRequest productRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> showOrderPoint(long orderId, Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/orders/" + orderId + "/points")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteCartItem(Long cartItemId, Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createOrder(List<Long> cartItemIds, Member member, int usedPoint) {
        OrderRequest orderRequest = new OrderRequest(cartItemIds, usedPoint);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestGetCartItems(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> showUserPoint(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/points")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> showUserOrderHistory(Member member) {
        return given()
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/orders")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> showOrderDetailHistory(long orderId, Member member) {
        return given()
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .pathParam("id", orderId)
                .when()
                .get("/orders/{id}")
                .then()
                .extract();
    }
}
