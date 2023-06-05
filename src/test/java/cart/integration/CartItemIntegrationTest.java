package cart.integration;

import cart.Repository.MemberRepository;
import cart.domain.Member.Member;
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

import static cart.integration.apifixture.ApiFixture.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = addProductAndGetId(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = addProductAndGetId(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberRepository.getMemberById(1L);
        member2 = memberRepository.getMemberById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        ExtractableResponse<Response> response = requestAddCartItem(member, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        Long cartItemId1 = addCartItemAndGetId(member, new CartItemRequest(productId));
        Long cartItemId2 = addCartItemAndGetId(member, new CartItemRequest(productId2));

        ExtractableResponse<Response> response = requestGetCartItems(member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsAll(Arrays.asList(cartItemId1, cartItemId2));
    }

    @DisplayName("장바구니에 아무것도 들어있지 않은 경우도 정상동작 한다.")
    @Test
    void getCartItemsWhenNoItemInCart() {
        // when
        ExtractableResponse<Response> response = requestGetCartItems(member);
        List<Long> resultCartItemIds = response.jsonPath().getList("$");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultCartItemIds.isEmpty()).isTrue();
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        Long cartItemId = addCartItemAndGetId(member, new CartItemRequest(productId));

        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 10);

        ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedCartItemResponse.isPresent()).isTrue();
        assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(10);
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() {
        Long cartItemId = addCartItemAndGetId(member, new CartItemRequest(productId));

        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member, cartItemId, 0);

        ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        Optional<CartItemResponse> selectedCartItemResponse = cartItemsResponse.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedCartItemResponse.isPresent()).isFalse();
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        Long cartItemId = addCartItemAndGetId(member, new CartItemRequest(productId));
        ExtractableResponse<Response> response = requestDeleteCartItem(cartItemId, member);
        ExtractableResponse<Response> cartItemsResponse = requestGetCartItems(member);

        var cartItems = cartItemsResponse.jsonPath().getList("$");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(cartItems.size()).isEqualTo(0);
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        Member illegalMember = new Member(member.getId(), member.getEmail().email(), member.getPassword().password() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        ExtractableResponse<Response> response = requestAddCartItem(illegalMember, cartItemRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("헤더에 사용자 정보가 저장되어 있지 않을때 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemWithoutMemberInformation() {
        CartItemRequest cartItemRequest = new CartItemRequest(productId);

        var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() {
        Long cartItemId = addCartItemAndGetId(member, new CartItemRequest(productId));
        ExtractableResponse<Response> response = requestUpdateCartItemQuantity(member2, cartItemId, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템을 삭제하면 실패한다.")
    @Test
    void deleteOtherMembersCartItem() {
        Long cartItemId = addCartItemAndGetId(member, new CartItemRequest(productId));
        ExtractableResponse<Response> response = requestDeleteCartItem(cartItemId, member2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
