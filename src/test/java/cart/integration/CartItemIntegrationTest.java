package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.request.ProductRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.LoginResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CartItemIntegrationTest extends IntegrationTest {

    private Long productId1;
    private Long productId2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        //given
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(cartItemRequest)
            .auth().oauth2(getAccessToken("a@a.com", "1234"))
            .when().post("/cart-items")
            .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }


    @DisplayName("잘못된 토큰 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        //given
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(cartItemRequest)
            .auth().oauth2(getAccessToken("a@a.com", "1234") + "i")
            .when().post("/cart-items")
            .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        //given
        final CartItemRequest cartItemRequest1 = new CartItemRequest(productId1);
        final String accessToken = getAccessToken("a@a.com", "1234");
        final Long cartItemId = requestAddCartItemAndGetId(accessToken, cartItemRequest1);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .auth().oauth2(accessToken)
            .when().delete("/cart-items/{id}", cartItemId)
            .then()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        //given
        CartItemRequest cartItemRequest1 = new CartItemRequest(productId1);
        CartItemRequest cartItemRequest2 = new CartItemRequest(productId2);

        final String accessToken = getAccessToken("a@a.com", "1234");
        requestAddCartItemAndGetId(accessToken, cartItemRequest1);
        requestAddCartItemAndGetId(accessToken, cartItemRequest2);

        //when
        final List<CartItemResponse> response = given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/cart-items")
            .then()
            .extract()
            .jsonPath().getList(".", CartItemResponse.class);

        //then
        assertThat(response).hasSize(2);
    }


    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        //given
        CartItemRequest cartItemRequest1 = new CartItemRequest(productId1);

        final String accessToken = getAccessToken("a@a.com", "1234");
        final Long cartItemId = requestAddCartItemAndGetId(accessToken, cartItemRequest1);
        final CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(4);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(cartItemQuantityUpdateRequest)
            .when().patch("/cart-items/{id}", cartItemId)
            .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() {
        //given
        final CartItemRequest cartItemRequest1 = new CartItemRequest(productId1);
        final String accessToken1 = getAccessToken("a@a.com", "1234");
        final String accessToken2 = getAccessToken("b@b.com", "1234");
        final Long cartItemId = requestAddCartItemAndGetId(accessToken1, cartItemRequest1);
        final CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(4);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken2)
            .body(cartItemQuantityUpdateRequest)
            .when().patch("/cart-items/{id}", cartItemId)
            .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }


    //
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

    private Long requestAddCartItemAndGetId(final String token, final CartItemRequest cartItemRequest) {
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .body(cartItemRequest)
            .when()
            .post("/cart-items")
            .then()
            .log().all()
            .extract();

        return getIdFromCreatedResponse(response);
    }

    public String getAccessToken(final String email, final String password) {
        final ResponseBodyExtractionOptions body = given().log().all()
            .formParam("email", email)
            .formParam("password", password)
            .when()
            .post("/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body();

        final LoginResponse loginResponse = body.as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}
