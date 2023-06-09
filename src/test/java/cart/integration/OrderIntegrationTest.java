package cart.integration;

import cart.domain.Member;
import cart.domain.product.Product;
import cart.dto.CartItemDto;
import cart.dto.ProductDto;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.request.CouponIssueRequest;
import cart.dto.request.CouponRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import cart.dto.response.ExceptionResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON_REQUEST;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON_REQUEST;
import static cart.domain.fixture.ProductFixture.PRODUCT_A;
import static cart.domain.fixture.ProductFixture.PRODUCT_B;
import static cart.domain.fixture.ProductFixture.PRODUCT_C;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product productA;
    private Product productB;
    private Product productC;
    private Long amountMemberCouponId;
    private Long rateMemberCouponId;
    private Member member;
    private Member wrongMember;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberRepository.getMemberById(1L);
        wrongMember = memberRepository.getMemberById(2L);

        productA = productRepository.getProductById(createProduct(PRODUCT_A));
        productB = productRepository.getProductById(createProduct(PRODUCT_B));
        productC = productRepository.getProductById(createProduct(PRODUCT_C));

        Long amountCouponId = createCoupon(AMOUNT_1000_COUPON_REQUEST);
        Long rateCouponId = createCoupon(RATE_10_COUPON_REQUEST);
        amountMemberCouponId = issueCoupon(member, amountCouponId);
        rateMemberCouponId = issueCoupon(member, rateCouponId);
    }

    @Test
    @DisplayName("상품 주문을 성공한다")
    void order_test() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productA.getId());
        OrderRequest orderRequest = new OrderRequest(
                List.of(new CartItemDto(cartItemId, 1, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );

        // when
        ExtractableResponse<Response> response = requestOrder(member, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("주문한 상품 정보를 불러온다")
    void get_order_info_test() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productA.getId());
        OrderRequest orderRequest = new OrderRequest(
                List.of(new CartItemDto(cartItemId, 1, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );
        long orderId = getIdFromCreatedResponse(requestOrder(member, orderRequest));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();

        // then
        int expectedOriginalPrice = productA.getPrice();
        OrderResponse orderResponse = response.body().as(OrderResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(orderResponse.getId()).isEqualTo(orderId);
            softly.assertThat(orderResponse.getOriginalPrice()).isEqualTo(expectedOriginalPrice);
            softly.assertThat(orderResponse.getActualPrice()).isEqualTo((int) (expectedOriginalPrice*0.9));
            softly.assertThat(orderResponse.getDeliveryFee()).isEqualTo(3000);
            softly.assertThat(orderResponse.getCartItems()).hasSize(1);
            CartItemDto item = orderResponse.getCartItems().get(0);
            softly.assertThat(item.getQuantity()).isEqualTo(1);
            softly.assertThat(item.getProduct().getId()).isEqualTo(productA.getId());
            softly.assertThat(item.getProduct().getName()).isEqualTo(productA.getName());
            softly.assertThat(item.getProduct().getPrice()).isEqualTo(productA.getPrice());
            softly.assertThat(item.getProduct().getImageUrl()).isEqualTo(productA.getImageUrl());
        });
    }

    @Test
    @DisplayName("주문 정보들을 불러온다")
    void get_all_order_info_test() {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member, productA.getId());
        Long cartItemId2 = requestAddCartItemAndGetId(member, productA.getId());
        requestUpdateCartItemQuantity(member, cartItemId2, 3);
        OrderRequest orderRequest1 = new OrderRequest(
                List.of(new CartItemDto(cartItemId1, 1, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );
        requestOrder(member, orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                List.of(new CartItemDto(cartItemId2, 3, ProductDto.of(productA))),
                3000,
                List.of(amountMemberCouponId)
        );
        requestOrder(member, orderRequest2);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        // then
        OrdersResponse ordersResponse = response.body().as(OrdersResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(ordersResponse.getOrders()).hasSize(2);
        });
    }

    @Test
    @DisplayName("잘못된 사용자가 주문정보 조회시 권한 예외를 반환한다")
    void wrong_member_order_request_test() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productA.getId());
        OrderRequest orderRequest = new OrderRequest(
                List.of(new CartItemDto(cartItemId, 1, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );
        long orderId = getIdFromCreatedResponse(requestOrder(member, orderRequest));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(wrongMember.getEmail(), wrongMember.getPassword())
                .body(orderRequest)
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("실제 카트정보와 다른 수량으로 주문요청시 예외를 반환한다")
    void order_quantity_not_match_test() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productA.getId());
        OrderRequest orderRequest = new OrderRequest(
                List.of(new CartItemDto(cartItemId, 2, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );

        // when
        ExtractableResponse<Response> response = requestOrder(member, orderRequest);

        // then
        ExceptionResponse responseBody = response.body().as(ExceptionResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(responseBody.getMessage()).contains("주문하려는 상품의 수량을 다시 확인해 주세요. 수량이 변경된 상품 아이디 : ");
        });
    }

    @Test
    @DisplayName("주문상품의 가격이 변경되었을 시 예외를 반환한다")
    void order_product_price_not_match_test() {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productA.getId());
        Product wrongProduct = new Product(productA.getId(), productA.getName(), productA.getPrice()-100, productA.getImageUrl());
        OrderRequest orderRequest = new OrderRequest(
                List.of(new CartItemDto(cartItemId, 1, ProductDto.of(wrongProduct))),
                3000,
                List.of(rateMemberCouponId)
        );

        // when
        ExtractableResponse<Response> response = requestOrder(member, orderRequest);

        // then
        ExceptionResponse responseBody = response.body().as(ExceptionResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(responseBody.getMessage()).contains("주문하려는 상품의 가격이 변경되었습니다. 달라진 상품 아이디 : ");
        });
    }

    @Test
    @DisplayName("이미 사용한 쿠폰을 이용해 주문시 예외를 반환한다")
    void use_already_used_coupon_test() {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member, productA.getId());
        Long cartItemId2 = requestAddCartItemAndGetId(member, productA.getId());
        requestUpdateCartItemQuantity(member, cartItemId2, 3);
        OrderRequest orderRequest1 = new OrderRequest(
                List.of(new CartItemDto(cartItemId1, 1, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );
        requestOrder(member, orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                List.of(new CartItemDto(cartItemId2, 3, ProductDto.of(productA))),
                3000,
                List.of(rateMemberCouponId)
        );

        // when
        ExtractableResponse<Response> response = requestOrder(member, orderRequest2);

        // then
        ExceptionResponse responseBody = response.body().as(ExceptionResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(responseBody.getMessage()).contains("이미 사용된 쿠폰입니다.");
        });
    }

    private ExtractableResponse<Response> requestOrder(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    private Long createProduct(Product product) {
        ProductRequest productRequest = new ProductRequest(product.getName(), product.getPrice(), product.getImageUrl());
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

    private Long requestAddCartItemAndGetId(Member member, Long productId) {
        ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId));
        return getIdFromCreatedResponse(response);
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

    private Long createCoupon(CouponRequest couponRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when()
                .post("/coupons")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private Long issueCoupon(Member member, Long couponId) {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId);
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(couponIssueRequest)
                .post("/coupons/me")
                .then()
                .log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }
}
