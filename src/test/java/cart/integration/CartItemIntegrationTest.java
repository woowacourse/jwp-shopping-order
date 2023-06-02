package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberRepository.findById(1L);
        member2 = memberRepository.findById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        final CartItemRequest cartItemRequest = new CartItemRequest(productId, 1);
        final ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("장바구니에 여러개 아이템을 추가한다.")
    @Test
    void addCartItems() {
        final CartItemRequest cartItemRequest = new CartItemRequest(productId, 10);
        final ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("장바구니에 두번 아이템을 추가한다.")
    @Test
    void addCartItemTwice() {
        final CartItemRequest cartItemRequest = new CartItemRequest(productId, 1);
        final ExtractableResponse<Response> response1 = requestAddCartItem(member, cartItemRequest);
        final ExtractableResponse<Response> response2 = requestAddCartItem(member, cartItemRequest);

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        final Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        final CartItemRequest cartItemRequest = new CartItemRequest(productId, 1);
        final ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        final Long cartItemId1 = requestAddCartItemAndGetId(member, productId);
        final Long cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        final ExtractableResponse<Response> response = requestGetCartItems(member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
            .map(CartItemResponse::getId)
            .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        final Long cartItemId = requestAddCartItemAndGetId(member, productId);

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 10);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
            .getList(".", CartItemResponse.class)
            .stream()
            .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
            .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isTrue();
        assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(10);
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() {
        final Long cartItemId = requestAddCartItemAndGetId(member, productId);

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 0);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
            .getList(".", CartItemResponse.class)
            .stream()
            .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
            .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() {
        final Long cartItemId = requestAddCartItemAndGetId(member, productId);

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        final Long cartItemId = requestAddCartItemAndGetId(member, productId);

        final ExtractableResponse<Response> response = requestDeleteCartItem(cartItemId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
            .getList(".", CartItemResponse.class)
            .stream()
            .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
            .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    private Long createProduct(final ProductRequest productRequest) {
        final ExtractableResponse<Response> response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when()
            .post("/products")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddCartItem(final Member member,
        final CartItemRequest cartItemRequest) {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .body(cartItemRequest)
            .when()
            .post("/cart-items")
            .then()
            .log().all()
            .extract();
    }

    private Long requestAddCartItemAndGetId(final Member member, final Long productId) {
        final ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId, 1));
        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetCartItems(final Member member) {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .log().all()
            .extract();
    }

    private ExtractableResponse<Response> requestUpdateCartItemQuantity(final Member member, final Long cartItemId,
        final int quantity) {
        final CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .body(quantityUpdateRequest)
            .patch("/cart-items/{cartItemId}", cartItemId)
            .then()
            .log().all()
            .extract();
    }

    private ExtractableResponse<Response> requestDeleteCartItem(final Long cartItemId) {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .delete("/cart-items/{cartItemId}", cartItemId)
            .then()
            .log().all()
            .extract();
    }
}
