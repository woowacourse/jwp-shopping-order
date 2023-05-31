package cart.integration;

import cart.application.dto.request.CartItemQuantityUpdateRequest;
import cart.application.dto.request.CartItemRequest;
import cart.application.dto.request.PaymentRequest;
import cart.application.dto.request.ProductRequest;
import cart.application.dto.response.CartItemResponse;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Product product1;
    private Product product2;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        product1 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
        product2 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");

        final MemberEntity memberEntity1 = memberDao.getMemberById(1L);
        final MemberEntity memberEntity2 = memberDao.getMemberById(2L);
        this.member1 = new Member(
                memberEntity1.getId(),
                memberEntity1.getEmail(),
                memberEntity1.getPassword(),
                memberEntity1.getPoint()
        );
        member2 = new Member(
                memberEntity2.getId(),
                memberEntity2.getEmail(),
                memberEntity2.getPassword(),
                memberEntity2.getPoint()
        );
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        final CartItemRequest cartItemRequest = new CartItemRequest(product1.getId());
        final ExtractableResponse<Response> response = requestAddCartItem(member1, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        final Member illegalMember = new Member(member1.getId(), member1.getEmail(), member1.getPassword() + "asdf", member1.getPoint());
        final CartItemRequest cartItemRequest = new CartItemRequest(product1.getId());
        final ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        final Long cartItemId1 = requestAddCartItemAndGetId(member1, product1.getId());
        final Long cartItemId2 = requestAddCartItemAndGetId(member1, product2.getId());

        final ExtractableResponse<Response> response = requestGetCartItems(member1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        final Long cartItemId = requestAddCartItemAndGetId(member1, product1.getId());

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member1, cartItemId, 10);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member1);

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
        final Long cartItemId = requestAddCartItemAndGetId(member1, product1.getId());

        final ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member1, cartItemId, 0);
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

    @DisplayName("장바구니에 담긴 아이템 결제를 테스트한다.")
    @Nested
    class PaymentTest {

        @DisplayName("장바구니가 비어있을 경우 bad request를 반환한다.")
        @Test
        void emptyCartPayment() {
            final PaymentRequest request = new PaymentRequest(new ArrayList<>(), 0);

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                    .body(request)
                    .when()
                    .post("/cart-items/payment")
                    .then()
                    .extract();


            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.asString()).isEqualTo("장바구니가 비어있습니다.")
            );
        }

        @DisplayName("포인트가 0보다 작을 경우 bad request를 반환한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -100, -987654321})
        void minusPointPayment(final int point) {
            final PaymentRequest request = new PaymentRequest(List.of(new CartItemRequest(1L)), point);

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                    .body(request)
                    .when()
                    .post("/cart-items/payment")
                    .then()
                    .extract();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.asString()).isEqualTo("포인트는 0원 이상 사용 가능합니다.")
            );
        }

        @DisplayName("사용자의 보유 포인트보다 사용 포인트가 클 경우 bad request를 반환한다.")
        @Test
        void overPointUsePayment() {
            final PaymentRequest request = new PaymentRequest(List.of(new CartItemRequest(1L)), 100);

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                    .body(request)
                    .when()
                    .post("/cart-items/payment")
                    .then()
                    .extract();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.asString()).isEqualTo("포인트가 부족합니다.")
            );
        }

        @DisplayName("장바구니 아이템을 담은 사용자와 결제 요청 사용자가 다를 경우 bad request를 반환한다.")
        @Test
        void differentMemberPayment() {
            final PaymentRequest request = new PaymentRequest(List.of(new CartItemRequest(1L)), 100);

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                    .body(request)
                    .when()
                    .post("/cart-items/payment")
                    .then()
                    .extract();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                    () -> assertThat(response.asString()).isEqualTo("장바구니 아이템 소유자와 사용자가 다릅니다.")
            );
        }

        @DisplayName("장바구니에 담긴 아이템을 결제한다.")
        @Test
        void paymentCartItems() {
            final PaymentRequest request = new PaymentRequest(List.of(new CartItemRequest(1L)), 0);
            final int expectPoint = 1_000;

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                    .body(request)
                    .when()
                    .redirects().follow(false)
                    .post("/cart-items/payment")
                    .then()
                    .extract();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.header("Location")).isEqualTo("redirect:/orders/histories/1"),
                    // point = price * quantity * point_rate
                    () -> assertThat(memberDao.getMemberById(member1.getId()).getPoint()).isEqualTo(expectPoint)
            );
        }

        @DisplayName("포인트를 사용할 경우 포인트가 차감된다.")
        @Test
        void paymentWithPoint() {
            final int expectPoint = 1400;
            final PaymentRequest request = new PaymentRequest(List.of(new CartItemRequest(1L)), 100);
            member1.savePoint(500);
            memberDao.updatePoint(new MemberEntity(member1.getId(), null, null, member1.getPoint(), null));

            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                    .body(request)
                    .when()
                    .redirects().follow(false)
                    .post("/cart-items/payment")
                    .then()
                    .extract();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.header("Location")).isEqualTo("redirect:/orders/histories/1"),
                    () -> assertThat(memberDao.getMemberById(member1.getId()).getPoint()).isEqualTo(expectPoint)
            );
        }
    }
}
