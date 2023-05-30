package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(ProductRequest.of("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(ProductRequest.of("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @Test
    void 사용자가_담은_장바구니_아이템을_조회한다() {
        final Long cartItemId1 = requestAddCartItemAndGetId(member, productId);
        final Long cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        final ExtractableResponse<Response> response = requestGetCartItems(member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    @Test
    void 장바구니에_아이템을_추가한다() {
        final CartItemRequest cartItemRequest = CartItemRequest.from(productId);
        final ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 잘못된_사용자_정보로_장바구니에_아이템을_추가_요청시_실패한다() {
        final Member illegalMember = Member.of(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        final CartItemRequest cartItemRequest = CartItemRequest.from(productId);
        final ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_변경한다() {
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

    @Test
    void 장바구니에_담긴_아이템의_수량을_0으로_변경하면_장바구니에서_아이템이_삭제된다() {
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

    @Test
    void 다른_사용자가_담은_장바구니_아이템의_수량을_변경하려_하면_실패한다() {
        final Long cartItemId = requestAddCartItemAndGetId(member, productId);

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 장바구니에_담긴_아이템을_삭제한다() {
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

    private Long createProduct(ProductRequest productRequest) {
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

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
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

    private Long requestAddCartItemAndGetId(Member member, Long productId) {
        final ExtractableResponse<Response> response = requestAddCartItem(member, CartItemRequest.from(productId));
        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetCartItems(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) {
        final CartItemQuantityUpdateRequest quantityUpdateRequest = CartItemQuantityUpdateRequest.from(quantity);
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

    private ExtractableResponse<Response> requestDeleteCartItem(Long cartItemId) {
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
