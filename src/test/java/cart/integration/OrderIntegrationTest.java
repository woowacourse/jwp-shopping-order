package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.repository.MemberRepository;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private CartItemRequest cartItemRequest1;
    private CartItemRequest cartItemRequest2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        final ProductRequest productRequest1 = new ProductRequest("치킨", 15_000, "chicken.png");
        final Long productId1 = createProduct(productRequest1);
        final ProductRequest productRequest2 = new ProductRequest("피자", 20_000, "pizza.png");
        final Long productId2 = createProduct(productRequest2);
        member = memberRepository.findById(1L);
        cartItemRequest1 = new CartItemRequest(productId1, 3);
        createCartItem(cartItemRequest1);
        cartItemRequest2 = new CartItemRequest(productId2, 1);
        createCartItem(cartItemRequest2);
    }

    @DisplayName("쿠폰을 사용하지 않고 주문한다.")
    @Test
    void orderWithoutCoupon() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemRequest1, cartItemRequest2),
                65_000, 2_000, "서울특별시 송파구", null);

        // when
        final ExtractableResponse<Response> response = order(orderRequest);

        // then
        final OrderResponse orderResponse = response.as(OrderResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderResponse.getTotalProductAmount()).isEqualTo(65_000);
        assertThat(orderResponse.getDiscountedProductAmount()).isEqualTo(65_000);
        assertThat(orderResponse.getDeliveryAmount()).isEqualTo(2_000);
    }

    @DisplayName("주문하면 장바구니에서 해당 상품이 삭제된다.")
    @Test
    void orderThenDeleteCartItem() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemRequest1, cartItemRequest2),
                65_000, 2_000, "서울특별시 송파구", null);

        // when
        final ExtractableResponse<Response> response = order(orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<CartItemResponse> cartItemResponses = findCartItems();
        final boolean isEmpty = cartItemResponses.stream()
                .filter(it -> it.getProduct().getId().equals(cartItemRequest1.getProductId())
                        || it.getProduct().getId().equals(cartItemRequest2.getProductId()))
                .findAny()
                .isEmpty();
        assertThat(isEmpty).isTrue();
    }

    private Long createProduct(final ProductRequest productRequest1) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest1)
                .when().post("/products")
                .then().extract();
        return Long.valueOf(response.header("Location").split("/")[2]);
    }

    private void createCartItem(final CartItemRequest cartItemRequest) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when().post("/cart-items")
                .then().extract();
    }

    private List<CartItemResponse> findCartItems() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/cart-items")
                .then().extract();
        return response.jsonPath().getList(".", CartItemResponse.class);
    }

    private ExtractableResponse<Response> order(final OrderRequest orderRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then().extract();
    }
}
