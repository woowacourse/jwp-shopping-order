package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.domain.Member;
import cart.service.request.CartItemRequest;
import cart.service.response.CartItemResponse;
import cart.service.request.ProductRequest;
import cart.service.response.ProductResponse;
import cart.repository.MemberRepository;
import cart.service.request.OrderRequestDto;
import cart.service.response.OrderProductResponseDto;
import cart.service.response.OrderResponseDto;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findById(1L);
    }

    @DisplayName("장바구니에 있는 품목을 주문한다.")
    @Test
    void orderProducts() {
        // given
        // 물품들이 등록되어 있다.
        final ProductRequest product1 = new ProductRequest("치킨", 10_000, "chicken.png");
        final Long product1Id = createProduct(product1);
        final ProductRequest product2 = new ProductRequest("사과", 6_000, "apple.png");
        final Long product2Id = createProduct(product2);

        // 장바구니가 있다.
        final Long cartItem1Id = requestAddCartItemAndGetId(member, product1Id);
        final Long cartItem2Id = requestAddCartItemAndGetId(member, product2Id);

        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(cartItem1Id, cartItem2Id));

        // when
        // 사용자는 장바구니에 있는 물품들을 선택해서 주문한다.
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequestDto)
                .when().post("/cart-items/order")
                .then().statusCode(HttpStatus.CREATED.value()).extract();

        final OrderResponseDto result = response.as(OrderResponseDto.class);

        // then
        // 사용자는 결제 결과를 응답받는다 // TODO : 주문하기
        assertThat(result.getTotalPrice())
                .isEqualTo(product1.getPrice() + product2.getPrice());
        assertThat(result.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponse)
                .extracting(ProductResponse::getName, ProductResponse::getPrice, ProductResponse::getImageUrl)
                .containsExactly(
                        tuple(product1.getName(), product1.getPrice(), product1.getImageUrl()),
                        tuple(product2.getName(), product2.getPrice(), product2.getImageUrl())
                );

        // 주문한 물품을 장바구니에서 삭제한다. // TODO : 주문하기
        final ExtractableResponse<Response> cartItems = requestGetCartItems(member);
        final List<CartItemResponse> cartItemResponse = cartItems.as(new TypeRef<>() {
        });
        assertThat(cartItemResponse).isEmpty();

        // 주문내역을 저장한다. // TODO : 주문 내역 확인
        final OrderResponseDto orderResponse = given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + result.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(OrderResponseDto.class);

        assertThat(orderResponse.getTotalPrice())
                .isEqualTo(product1.getPrice() + product2.getPrice());
        assertThat(orderResponse.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponse)
                .extracting(ProductResponse::getName, ProductResponse::getPrice, ProductResponse::getImageUrl)
                .containsExactly(
                        tuple(product1.getName(), product1.getPrice(), product1.getImageUrl()),
                        tuple(product2.getName(), product2.getPrice(), product2.getImageUrl())
                );
    }

    @DisplayName("주문 Id로 주문 내역을 조회한다.")
    @Test
    void findOrder() {

    }


    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/products")
                .then().statusCode(HttpStatus.CREATED.value()).extract();

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
                .when().post("/cart-items").then()
                .log().all().extract();
    }

    private Long requestAddCartItemAndGetId(Member member, Long productId) {
        ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId));
        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetCartItems(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE).auth().preemptive()
                .basic(member.getEmail(), member.getPassword())
                .when().get("/cart-items")
                .then().log().all().statusCode(HttpStatus.OK.value()).extract();
    }
}
