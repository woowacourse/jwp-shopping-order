package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
import cart.exception.AuthenticationException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        this.productId = this.createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        this.productId2 = this.createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        this.member = this.memberDao.getMemberById(1L).orElseThrow(AuthenticationException.NotFound::new);
        this.member2 = this.memberDao.getMemberById(1L).orElseThrow(AuthenticationException.NotFound::new);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        final CartItemRequest cartItemRequest = new CartItemRequest(this.productId);
        final ExtractableResponse<Response> response = this.requestAddCartItem(this.member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        final Member illegalMember = new Member(this.member.getId(), this.member.getEmail(), this.member.getPassword() + "asdf");
        final CartItemRequest cartItemRequest = new CartItemRequest(this.productId);
        final ExtractableResponse<Response> response = this.requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        final Long cartItemId1 = this.requestAddCartItemAndGetId(this.member, this.productId);
        final Long cartItemId2 = this.requestAddCartItemAndGetId(this.member, this.productId2);

        final ExtractableResponse<Response> response = this.requestGetCartItems(this.member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        final Long cartItemId = this.requestAddCartItemAndGetId(this.member, this.productId);

        final ExtractableResponse<Response> response = this.requestUpdateCartItemQuantity(this.member, cartItemId, 10);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = this.requestGetCartItems(this.member);

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
        final Long cartItemId = this.requestAddCartItemAndGetId(this.member, this.productId);

        final ExtractableResponse<Response> response = this.requestUpdateCartItemQuantity(this.member, cartItemId, 0);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = this.requestGetCartItems(this.member);

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
        final Long cartItemId = this.requestAddCartItemAndGetId(this.member, this.productId);

        final ExtractableResponse<Response> response = this.requestUpdateCartItemQuantity(this.member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        final Long cartItemId = this.requestAddCartItemAndGetId(this.member, this.productId);

        final ExtractableResponse<Response> response = this.requestDeleteCartItem(cartItemId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        final ExtractableResponse<Response> cartItemsResponse = this.requestGetCartItems(this.member);

        final Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    private Long createProduct(final ProductRequest productRequest) {
        final ExtractableResponse<Response> response = given(this.spec)
                .filter(document("create-product"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return this.getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddCartItem(final Member member, final CartItemRequest cartItemRequest) {
        return given(this.spec).log().all()
                .filter(
                        document("add-cart-item",
                                requestFields(
                                        fieldWithPath("productId").description("추가할 상품 id")
                                )
                        )
                )
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
        final ExtractableResponse<Response> response = this.requestAddCartItem(member, new CartItemRequest(productId));
        return this.getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetCartItems(final Member member) {
        return given(this.spec).log().all()
                .filter(document("add-cart-item"))
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
        return given(this.spec).log().all()
                .filter(
                        document("update-cart-item-quantity",
                                pathParameters(
                                        parameterWithName("cartItemId").description("수량을 변경할 장바구니 상품 id")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").description("설정할 수량")
                                )
                        )
                )
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
        return given(this.spec).log().all()
                .filter(
                        document("delete-cart-item",
                                pathParameters(
                                        parameterWithName("cartItemId").description("삭제할 장바구니 상품 id")
                                )
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
