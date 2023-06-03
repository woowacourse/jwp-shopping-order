package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.PagedCartItemsResponse;
import cart.dto.PaginationInfoDto;
import cart.dto.product.ProductRequest;
import cart.repository.dao.MemberDao;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemService cartItemService;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템 중 특정 페이지를 조회한다")
    @Test
    void getPagedCartItems() {
        // given
        int unitSize = 2;
        int page = 1;
        var allProducts = cartItemService.findByMember(member);
        int expectedTotalPage = 1;

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .queryParam("unit-size", unitSize)
                .queryParam("page", page)
                .when().get("/cart-items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        PagedCartItemsResponse pagedCartItemsResponse = response.as(PagedCartItemsResponse.class);
        final PaginationInfoDto pagination = pagedCartItemsResponse.getPagination();

        // then
        assertThat(pagedCartItemsResponse.getCartItems()).hasSize(unitSize);
        assertThat(pagination.getPerPage()).isEqualTo(unitSize);
        assertThat(pagination.getCurrentPage()).isEqualTo(page);
        assertThat(pagination.getLastPage()).isEqualTo(expectedTotalPage);
        assertThat(pagination.getTotal()).isEqualTo(allProducts.size());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 단건 조회한다.")
    @Test
    void getCartItemById() {
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId);
        requestAddCartItemAndGetId(member, productId2);

        ExtractableResponse<Response> response = requestGetCartItemById(member, cartItemId1);
        final CartItemResponse cartItemResponse = response.as(CartItemResponse.class);

        assertThat(cartItemResponse.getId()).isEqualTo(cartItemId1);
        assertThat(cartItemResponse.getProduct().getId()).isEqualTo(productId);
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        Long cartItemId = requestAddCartItemAndGetId(member, productId);

        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 10);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<CartItemResponse> cartItemsResponse = cartItemService.findByMember(member);

        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isTrue();
        assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(10);
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() {
        Long cartItemId = requestAddCartItemAndGetId(member, productId);

        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 0);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<CartItemResponse> cartItemsResponse = cartItemService.findByMember(member);

        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() {
        Long cartItemId = requestAddCartItemAndGetId(member, productId);

        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        Long cartItemId = requestAddCartItemAndGetId(member, productId);

        ExtractableResponse<Response> response = requestDeleteCartItem(cartItemId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        final List<CartItemResponse> cartItemsResponse = cartItemService.findByMember(member);

        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    private ExtractableResponse<Response> requestGetCartItemById(final Member member, final Long id) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/cart-items/{cartItemId}", id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

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
        ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId));
        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
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
