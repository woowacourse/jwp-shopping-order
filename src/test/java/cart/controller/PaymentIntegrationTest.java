package cart.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderProductResponseDto;
import cart.controller.response.OrderResponseDto;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
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

public class PaymentIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.getMemberById(1L);
    }

    @DisplayName("장바구니에 있는 품목을 주문한다.")
    @Test
    void orderProducts() {
        // given
        // 물품들이 등록되어 있다.
        int PRICE = 1_000_000_000;
        ProductRequest 홍실 = new ProductRequest("홍실", PRICE, "hongsil.com");
        Long hongSilId = createProduct(홍실);
        ProductRequest 매튜 = new ProductRequest("매튜", PRICE, "matthew.com");
        Long matthewId = createProduct(매튜);

        // 장바구니가 있다.
        Long hongCartItemId = requestAddCartItemAndGetId(member, hongSilId);
        Long mattCartItemId = requestAddCartItemAndGetId(member, matthewId);

        OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(hongCartItemId, mattCartItemId));

        // when
        // 사용자는 장바구니에 있는 물품들을 선택해서 주문한다.
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequestDto)
                .when().post("/cart-items/order")
                .then().extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
        OrderResponseDto result = response.as(OrderResponseDto.class);

        // then
        // 사용자는 결제 결과를 응답받는다.
        assertThat(result.getTotalPrice())
                .isEqualTo(PRICE * 2);
        assertThat(result.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponse)
                .extracting(ProductResponse::getName, ProductResponse::getPrice, ProductResponse::getImageUrl)
                .containsExactly(tuple("홍실", PRICE, "hongsil.com"), tuple("매튜", PRICE, "matthew.com"));

        // 주문한 물품을 장바구니에서 삭제한다.
        ExtractableResponse<Response> cartItems = requestGetCartItems(member);
        List<CartItemResponse> cartItemResponse = cartItems.as(new TypeRef<>() {});
        assertThat(cartItemResponse).isEmpty();

        // 주문내역을 저장한다.
        OrderResponseDto orderResponse = given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + result.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(OrderResponseDto.class);

        assertThat(orderResponse.getTotalPrice()).isEqualTo(PRICE * 2);
        assertThat(orderResponse.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponse)
                .extracting(ProductResponse::getName, ProductResponse::getPrice, ProductResponse::getImageUrl)
                .containsExactly(tuple("홍실", PRICE, "hongsil.com"), tuple("매튜", PRICE, "matthew.com"));
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
