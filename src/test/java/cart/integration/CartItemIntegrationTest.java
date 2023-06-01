package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long productId1;
    private Long productId2;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg", 10));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg", 20));

        memberRepository.save(new Member("a@a.com", "password1", new Point(0)));
        memberRepository.save(new Member("b@b.com", "password2", new Point(0)));
        member1 = memberRepository.findById(1L);
        member2 = memberRepository.findById(2L);
    }

    private Long createProduct(final ProductRequest productRequest) {
        var response = given()
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

    @Test
    void 장바구니에_아이템을_추가한다() {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        // when
        var response = requestAddCartItem(member1, cartItemRequest);
        long savedId = getIdFromCreatedResponse(response);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/cart-items/" + savedId);
    }

    private ExtractableResponse<Response> requestAddCartItem(final Member member, final CartItemRequest cartItemRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .extract();
    }

    @Test
    void 사용자가_담은_장바구니_아이템을_조회한다() {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member1, productId1);
        Long cartItemId2 = requestAddCartItemAndGetId(member1, productId2);

        // when
        var response = requestGetCartItems(member1);

        List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getCartItemId)
                .collect(Collectors.toList());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    private Long requestAddCartItemAndGetId(final Member member, final Long productId) {
        var response = requestAddCartItem(member, new CartItemRequest(productId));

        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetCartItems(final Member member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .extract();
    }

    @Test
    void 틀린_비밀번호로_장바구니에_아이템을_추가_요청시_실패한다() {
        // given
        Member illegalMember = new Member(member1.getId(), member1.getEmail(), member1.getPassword() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        // when
        var response = requestAddCartItem(illegalMember, cartItemRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    @Test
    void 장바구니에_담긴_아이템의_수량을_변경한다() {
        // given
        int quantity = 99;
        Long cartItemId = requestAddCartItemAndGetId(member1, productId1);

        // when
        var response = requestUpdateCartItemQuantity(member1, cartItemId, quantity);
        var cartItemsResponse = requestGetCartItems(member1);

        var selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getCartItemId().equals(cartItemId))
                .findFirst();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedCartItemResponse.isPresent()).isTrue();
        assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(quantity);
    }

    private ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .extract();
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_0으로_변경하면_장바구니에서_아이템이_삭제된다() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member1, productId1);

        // when
        var response = requestUpdateCartItemQuantity(member1, cartItemId, 0);
        var cartItemsResponse = requestGetCartItems(member1);

        var selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getCartItemId().equals(cartItemId))
                .findFirst();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedCartItemResponse.isEmpty()).isTrue();
    }

    @Test
    void 다른_사용자가_담은_장바구니_아이템의_수량을_변경하려_하면_실패한다() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member1, productId1);

        // when
        var response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 장바구니에_담긴_아이템을_삭제한다() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member1, productId1);

        // when
        var response = requestDeleteCartItem(cartItemId);

        var cartItemsResponse = requestGetCartItems(member1);

        var selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getCartItemId().equals(cartItemId))
                .findFirst();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(selectedCartItemResponse.isEmpty()).isTrue();
    }

    private ExtractableResponse<Response> requestDeleteCartItem(Long cartItemId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .extract();
    }
}
