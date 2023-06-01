package cart.integration;

import cart.domain.Member;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderIntegerationTest extends IntegrationTest {

    private ProductRequest productRequest;
    private OrderRequest orderRequest;
    private CartItemRequest cartItemRequest;
    private Member member;
    private String encoded;

    @BeforeEach
    void setUp() {
        super.setUp();
        productRequest = new ProductRequest("치킨", 10000, "http://example.com/chicken.jpg");
        orderRequest = new OrderRequest(List.of(1L), null);
        cartItemRequest = new CartItemRequest(1L);
        member = new Member(1L, "a@a", "123");
        encoded = new String(Base64.getEncoder().encode("a@a:123".getBytes()));
    }

    @Test
    @DisplayName("카트에 담긴 상품을 주문할 수 있다.")
    void saveOrder() {
        saveProduct();
        saveCartItem();
        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .body(orderRequest)
                .when().post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("카트에 담기지 않은 상품을 주문할 경우 예외가 발생한다.")
    void saveOrder_exception() {
        saveProduct();
        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .body(orderRequest)
                .when().post("/orders")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private void saveCartItem() {
        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .body(cartItemRequest)
                .when().post("/cart-items")
                .then().extract();
    }

    private void saveProduct() {
        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/products");
    }
}
