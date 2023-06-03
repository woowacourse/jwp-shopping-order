package cart.controller.cart;

import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.repository.member.MemberRepository;
import cart.service.cart.CartService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/setData.sql")
class CartControllerIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("장바구니를 모두 조회한다.")
    @Test
    void find_all_carts() {
        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/cart-items");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].cartItemId", equalTo(1))
                .body("[0].quantity", equalTo(10))
                .body("[0].product.id", equalTo(1))
                .body("[0].product.name", equalTo("치킨"))
                .body("[0].product.imageUrl", equalTo("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"))
                .body("[0].product.isOnSale", equalTo(false))
                .body("[0].product.salePrice", equalTo(0));
    }

    @DisplayName("카트 아이템을 추가한다.")
    @Test
    void add_cart_item() {
        // given
        CartItemRequest req = new CartItemRequest(1L);

        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .contentType(ContentType.JSON)
                .body(req)
                .post("/cart-items");

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/cart-items/1");
    }

    @DisplayName("카트 아이템의 수량을 변경한다.")
    @Test
    void update_cart_item_quantity() {
        // given
        CartItemQuantityUpdateRequest req = new CartItemQuantityUpdateRequest(20);

        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .contentType(ContentType.JSON)
                .body(req)
                .patch("/cart-items/1");

        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("카트 아이템을 삭제한다.")
    @Test
    void delete_cart_item() {
        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .delete("/cart-items/1");

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("모든 카트 아이템을 삭제한다.")
    @Test
    void delete_all_cart_item() {
        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .delete("/cart-items");

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
