package cart.integration;

import cart.db.repository.MemberRepository;
import cart.domain.member.Member;
import cart.dto.product.ProductRequest;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.login.MemberRequest;
import cart.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.exception.ErrorCode.NOT_AUTHENTICATION_MEMBER;
import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long chickenId;
    private Long pizzaId;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        chickenId = 상품_생성_ID_반환(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        pizzaId = 상품_생성_ID_반환(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        회원가입_요청(new MemberRequest("member1", "1234"));
        회원가입_요청(new MemberRequest("member2", "1234"));

        member1 = memberRepository.findById(1L);
        member2 = memberRepository.findById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(member1, cartItemRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 ")
    @Test
    void addCartItem_ByIllegalMember() {
        // given
        Member illegalMember = new Member(member1.getId(), member1.getName(), member1.getPassword() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(illegalMember, cartItemRequest);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(NOT_AUTHENTICATION_MEMBER)
        );
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        // given
        Long cartItemId1 = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);
        Long cartItemId2 = 장바구니_아이템_추가_요청_후_ID_반환(member1, pizzaId);

        // when
        ExtractableResponse<Response> response = 사용자의_장바구니_아이템_조회(member1);

        // then
        List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2))
        );
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void updateCartItemQuantity() {
        // given
        Long cartItemId = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경_요청(member1, cartItemId, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Optional<CartItemResponse> selectedCartItemResponse = 사용자의_장바구니_아이템_조회(member1)
                .jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertAll(
                () -> assertThat(selectedCartItemResponse.isPresent()).isTrue(),
                () -> assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(10)
        );
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void updateCartItemQuantity_toZero() {
        // given
        Long cartItemId = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경_요청(member1, cartItemId, 0);
        Optional<CartItemResponse> selectedCartItemResponse = 사용자의_장바구니_아이템_조회(member1)
                .jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(selectedCartItemResponse.isPresent()).isFalse()
        );
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 NOT_AUTHORIZATION_MEMBER 예외가 발생한다.")
    @Test
    void updateCartItem_ByOtherMember() {
        // given
        Long cartItemId = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경_요청(member2, cartItemId, 10);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        // given
        Long cartItemId = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_삭제_요청(member1, cartItemId);
        ExtractableResponse<Response> cartItemsResponse = 사용자의_장바구니_아이템_조회(member1);
        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(selectedCartItemResponse.isPresent()).isFalse()
        );
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템을 삭제하려하면 NOT_AUTHORIZATION_MEMBER 예외가 발생한다.")
    @Test
    void removeCartItem_ByOtherMember() {
        // given
        Long cartItemId = 장바구니_아이템_추가_요청_후_ID_반환(member1, chickenId);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_삭제_요청(member2, cartItemId);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }

    private Long 상품_생성_ID_반환(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return 생성된_요청에_대한_ID_반환(response);
    }

    private void 회원가입_요청(MemberRequest memberRequest) {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/users/join")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private long 생성된_요청에_대한_ID_반환(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> 장바구니_아이템_추가_요청(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private Long 장바구니_아이템_추가_요청_후_ID_반환(Member member, Long productId) {
        ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(member, new CartItemRequest(productId));
        return 생성된_요청에_대한_ID_반환(response);
    }

    private ExtractableResponse<Response> 사용자의_장바구니_아이템_조회(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_수량_변경_요청(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_삭제_요청(Member member, Long cartItemId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
