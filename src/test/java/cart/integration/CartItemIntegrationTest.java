package cart.integration;

import cart.application.dto.request.CartItemQuantityUpdateRequest;
import cart.application.dto.request.CartItemRequest;
import cart.application.dto.response.CartItemResponse;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.TestFixture.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CartItemIntegrationTest extends IntegrationTest {

    private Product product1;
    private Member member1;
    private Member member2;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        super.setUp();

        product1 = PRODUCT1;
        member1 = getMember1();
        member2 = getMember2();
        cartItem1 = getCartItem1();
        cartItem2 = getCartItem2();
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        final CartItemRequest cartItemRequest = new CartItemRequest(product1.getId());
        final ExtractableResponse<Response> response = requestAddCartItem(member1, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("장바구니에 수량과 함께 아이템을 추가한다.")
    @Test
    void addWithQuantityCartItem() {
        final int quantity = 10;
        final CartItemRequest cartItemRequest = new CartItemRequest(product1.getId(), quantity);
        final ExtractableResponse<Response> response = requestAddCartItem(member1, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        final Member illegalMember = new Member(
                member1.getId(),
                member1.getEmail(),
                member1.getPassword() + "asdf",
                member1.getPoint()
        );
        final CartItemRequest cartItemRequest = new CartItemRequest(product1.getId());
        final ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        final ExtractableResponse<Response> response = requestGetCartItems(member1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItem1.getId(), cartItem2.getId()));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        final int quantity = 10;
        final Long cartItemId = cartItem1.getId();
        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member1, cartItemId, quantity);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member1);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isTrue();
        assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(quantity);
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() {
        final Long cartItemId = requestAddCartItemAndGetId(member1, product1.getId());

        final int quantity = 0;
        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member1, cartItemId, quantity);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member1);

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
        final Long cartItemId = requestAddCartItemAndGetId(member1, product1.getId());

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        final Long cartItemId = requestAddCartItemAndGetId(member1, product1.getId());

        final ExtractableResponse<Response> response = requestDeleteCartItem(cartItemId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member1);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddCartItem(final Member member, final CartItemRequest cartItemRequest) {
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
        final ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId));
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

    private ExtractableResponse<Response> requestUpdateCartItemQuantity(final Member member, final Long cartItemId, final int quantity) {
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
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
