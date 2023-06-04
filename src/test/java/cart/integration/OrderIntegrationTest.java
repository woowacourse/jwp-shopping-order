package cart.integration;

import cart.db.repository.MemberRepository;
import cart.domain.member.Member;
import cart.dto.product.ItemRequest;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.cart.CartItemResponse;
import cart.dto.coupon.OrderCouponResponse;
import cart.dto.login.MemberRequest;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static cart.exception.ErrorCode.INVALID_PRODUCT_ID;
import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long chickenId;
    private Long pizzaId;
    private Member member1;
    private Member member2;

    private OrderRequest order1;
    private OrderRequest order2;
    private OrderRequest invalidOrder;

    @BeforeEach
    void setUp() {
        super.setUp();

        chickenId = 상품_생성_ID_반환(new ProductRequest("치킨", 10000, "http://example.com/chicken.jpg"));
        pizzaId = 상품_생성_ID_반환(new ProductRequest("피자", 15000, "http://example.com/pizza.jpg"));

        회원가입_요청(new MemberRequest("member1", "1234"));
        회원가입_요청(new MemberRequest("member2", "1234"));

        member1 = memberRepository.findById(1L);
        member2 = memberRepository.findById(2L);

        // 치킨2, 피자4 + 10% 쿠폰 = 80_000, 8000원 할인
        order1 = new OrderRequest(
                List.of(
                        new ItemRequest(chickenId, 2),
                        new ItemRequest(pizzaId, 4)
                ), 1L
        );

        // 치킨10  = 100_000
        order2 = new OrderRequest(
                List.of(
                        new ItemRequest(chickenId, 10)
                )
        );

        Long invalidProductId = 3L;
        invalidOrder = new OrderRequest(
                List.of(
                        new ItemRequest(invalidProductId, 10)
                )
        );
    }

    @DisplayName("사용자의 주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 사용자_주문_요청(member1, order1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("존재하지 않는 상품 번호로 주문을 요청할 경우, INVALID_PRODUCT_ID 예외가 발생한다.")
    @Test
    void createOrder_ByInvalidProductId() {
        // when
        ExtractableResponse<Response> response = 사용자_주문_요청(member1, invalidOrder);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(INVALID_PRODUCT_ID)
        );
    }


    @DisplayName("사용자의 모든 주문 내역을 조회한다.")
    @Test
    void findOrdersByMember() {
        // given
        사용자_주문_요청(member1, order1);
        사용자_주문_요청(member1, order2);

        // when
        ExtractableResponse<Response> response = 사용자_주문_조회(member1);
        List<OrderResponse> orderResponses = response.jsonPath()
                .getList(".", OrderResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderResponses.size()).isEqualTo(2),
                () -> assertThat(orderResponses)
                        .flatExtracting(OrderResponse::getTotalPrice)
                        .containsExactly(80_000, 100_000),
                () -> assertThat(orderResponses)
                        .flatExtracting(OrderResponse::getCouponDiscountPrice)
                        .containsExactly(8_000, 0),
                () -> assertThat(orderResponses)
                        .flatExtracting(OrderResponse::getDiscountedTotalPrice)
                        .containsExactly(72_000, 100_000),
                () -> assertThat(orderResponses)
                        .flatExtracting(OrderResponse::getItems)
                        .extracting(CartItemResponse::getProduct)
                        .extracting(ProductResponse::getName)
                        .containsExactly("치킨", "피자", "치킨"),
                () -> assertThat(orderResponses)
                        .flatExtracting(OrderResponse::getItems)
                        .extracting(CartItemResponse::getQuantity)
                        .containsExactly(2, 4, 10),
                () -> assertThat(orderResponses)
                        .extracting(OrderResponse::getCoupon)
                        .filteredOn(Objects::nonNull)
                        .extracting(OrderCouponResponse::getName)
                        .containsExactly("신규 가입 할인 쿠폰")
        );
    }

    @DisplayName("사용자의 상세 주문 내역을 조회한다.")
    @Test
    void findOrderById() {
        // given
        String location = 사용자_주문_요청(member1, order1).header("location");


        // when
        ExtractableResponse<Response> response = 사용자_상세_주문_조회(member1, location);
        OrderResponse orderResponse = response.jsonPath()
                .getObject(".", OrderResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderResponse)
                        .extracting(OrderResponse::getTotalPrice, OrderResponse::getDiscountedTotalPrice, OrderResponse::getCouponDiscountPrice)
                        .containsExactly(80_000, 72_000, 8_000),
                () -> assertThat(orderResponse)
                        .extracting(OrderResponse::getItems)
                        .extracting(cartItemResponses ->
                                        cartItemResponses.get(0).getProduct().getName(),
                                cartItemResponses ->
                                        cartItemResponses.get(1).getProduct().getName())
                        .containsExactly("치킨", "피자"),
                () -> assertThat(orderResponse)
                        .extracting(OrderResponse::getItems)
                        .extracting(cartItemResponses ->
                                        cartItemResponses.get(0).getQuantity(),
                                cartItemResponses ->
                                        cartItemResponses.get(1).getQuantity())
                        .containsExactly(2, 4)
        );

    }


    @DisplayName("다른 사용자가 상세 주문 내역을 조회 시, NOT_AUTHORIZATION_MEMBER 예외가 발생한다,")
    @Test
    void findOrderById_ByOtherMember() {
        // given
        String location = 사용자_주문_요청(member1, order1).header("location");


        // when
        ExtractableResponse<Response> response = 사용자_상세_주문_조회(member2, location);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }

    private ExtractableResponse<Response> 사용자_주문_조회(final Member member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 사용자_상세_주문_조회(final Member member, final String location) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .get(location)
                .then()
                .extract();
    }


    private ExtractableResponse<Response> 사용자_주문_요청(final Member member, final OrderRequest orderRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    private long 생성된_요청에_대한_ID_반환(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private Long 상품_생성_ID_반환(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return 생성된_요청에_대한_ID_반환(response);
    }

    private void 회원가입_요청(MemberRequest memberRequest) {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/users/join")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
