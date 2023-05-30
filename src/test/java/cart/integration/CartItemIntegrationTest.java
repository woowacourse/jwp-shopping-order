package cart.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.application.dto.member.MemberLoginRequest;
import cart.application.dto.member.MemberSaveRequest;
import cart.application.dto.product.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CartItemIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("장바구니에 아이템을 추가한다.")
    void addCartItem() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        final ProductRequest 피자_등록_요청 = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
        상품_저장(치킨_등록_요청);
        상품_저장(피자_등록_요청);

        // expected
        final CartItemRequest 장바구니_저장_요청 = new CartItemRequest(1L);
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(져니_등록_요청.getName(), 져니_등록_요청.getPassword())
            .body(장바구니_저장_요청)
            .when()
            .post("/cart-items")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header(LOCATION, "/cart-items/" + 1);
    }

    @Test
    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    void addCartItemByIllegalMember() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        // expected
        final CartItemRequest 장바구니_저장_요청 = new CartItemRequest(1L);
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(져니_등록_요청.getName(), "test123")
            .body(장바구니_저장_요청)
            .when()
            .post("/cart-items")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    void getCartItems() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        final ProductRequest 피자_등록_요청 = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
        상품_저장(치킨_등록_요청);
        상품_저장(피자_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        final CartItemRequest 피자_장바구니_저장_요청 = new CartItemRequest(2L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);
        장바구니_상품_저장(져니_로그인_요청, 피자_장바구니_저장_요청);

        final CartItemQuantityUpdateRequest 장바구니_수량_수정_요청 = new CartItemQuantityUpdateRequest(10);
        장바구니_상품_수량_수정(져니_로그인_요청, 장바구니_수량_수정_요청, 1L);

        // expected
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].quantity", equalTo(10))
            .body("[0].product.id", equalTo(1))
            .body("[0].product.name", equalTo("치킨"))
            .body("[0].product.price", equalTo(10000))
            .body("[0].product.imageUrl", equalTo("http://example.com/chicken.jpg"))
            .body("[1].id", equalTo(2))
            .body("[1].quantity", equalTo(1))
            .body("[1].product.id", equalTo(2))
            .body("[1].product.name", equalTo("피자"))
            .body("[1].product.price", equalTo(15000))
            .body("[1].product.imageUrl", equalTo("http://example.com/pizza.jpg"));
    }

    @Test
    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    void increaseCartItemQuantity() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);

        // expected
        final CartItemQuantityUpdateRequest 장바구니_수량_수정_요청 = new CartItemQuantityUpdateRequest(10);
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .body(장바구니_수량_수정_요청)
            .patch("/cart-items/{cartItemId}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    @Test
    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    void decreaseCartItemQuantityToZero() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);

        final CartItemQuantityUpdateRequest 장바구니_수량_수정_요청 = new CartItemQuantityUpdateRequest(0);
        장바구니_상품_수량_수정(져니_로그인_요청, 장바구니_수량_수정_요청, 1L);

        // expected
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(0));
    }

    @Test
    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    void updateOtherMembersCartItem() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        final MemberSaveRequest 라온_등록_요청 = new MemberSaveRequest("raon", "jourzura!");
        사용자_저장(져니_등록_요청);
        사용자_저장(라온_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);

        // expected
        final CartItemQuantityUpdateRequest 장바구니_수량_수정_요청 = new CartItemQuantityUpdateRequest(10);
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(라온_등록_요청.getName(), 라온_등록_요청.getPassword())
            .when()
            .body(장바구니_수량_수정_요청)
            .patch("/cart-items/{cartItemId}", 1)
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
            .extract();
    }

    @Test
    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    void removeCartItem() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);

        // when
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .delete("/cart-items/{cartItemId}", 1)
            .then();

        // then
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(0));
    }

    @Test
    @DisplayName("장바구니에 담긴 아이템을 여러 개 삭제한다.")
    void removeCartItems() {
        // given
        final MemberSaveRequest 져니_등록_요청 = new MemberSaveRequest("journey", "test1234");
        사용자_저장(져니_등록_요청);

        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        final ProductRequest 피자_등록_요청 = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
        상품_저장(치킨_등록_요청);
        상품_저장(피자_등록_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_등록_요청.getName(), 져니_등록_요청.getPassword());
        final CartItemRequest 치킨_장바구니_저장_요청 = new CartItemRequest(1L);
        final CartItemRequest 피자_장바구니_저장_요청 = new CartItemRequest(2L);
        장바구니_상품_저장(져니_로그인_요청, 치킨_장바구니_저장_요청);
        장바구니_상품_저장(져니_로그인_요청, 피자_장바구니_저장_요청);

        // when
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .delete("/cart-items?ids=1,2")
            .then();

        // then
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(0));
    }
}
