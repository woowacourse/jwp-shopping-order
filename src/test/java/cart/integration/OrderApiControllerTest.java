package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import cart.ui.dto.request.CartItemRequest;
import cart.ui.dto.request.OrderRequest;
import cart.ui.dto.request.ProductRequest;
import cart.ui.dto.response.OrderProductResponse;
import cart.ui.dto.response.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderApiControllerTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CouponDao couponDao;

    private Long productId1;
    private Long productId2;
    private Member member;
    private Coupon coupon;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberDao.getMemberById(1L);

        coupon = couponDao.save(new Coupon("3000원 할인 쿠폰", Amount.of(3_000), Amount.of(10_000), false), member.getId());
    }

    @Test
    @DisplayName("장바구니의 물품들을 주문을 한다.")
    void testOrder() {
        //given
        final CartItemRequest cartItemRequest1 = new CartItemRequest(productId1, 3);
        final CartItemRequest cartItemRequest2 = new CartItemRequest(productId2, 3);
        final OrderRequest request = new OrderRequest(List.of(cartItemRequest1, cartItemRequest2), 75_000, 3_000,
            "address", coupon.getId());

        //when
        final ExtractableResponse<Response> response = order(request);
        final OrderResponse orderResponse = response.as(OrderResponse.class);

        //then
        final List<OrderProductResponse> products = List.of(
            new OrderProductResponse(productId1, "치킨", 10_000, "http://example.com/chicken.jpg", 3),
            new OrderProductResponse(productId2, "피자", 15_000, "http://example.com/pizza.jpg", 3));
        final OrderResponse expectedResponse = new OrderResponse(orderResponse.getId(), request.getTotalProductAmount(),
            72_000, request.getDeliveryAmount(), request.getAddress(), products);

        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    private ExtractableResponse<Response> order(final OrderRequest request) {
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .body(request)
            .post("/orders")
            .then()
            .log().all()
            .extract();
        return response;
    }

    @Test
    @DisplayName("id로 주문을 조회한다.")
    void testFindOrderById() {
        //given
        final CartItemRequest cartItemRequest1 = new CartItemRequest(productId1, 3);
        final CartItemRequest cartItemRequest2 = new CartItemRequest(productId2, 3);
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemRequest1, cartItemRequest2), 75_000, 3_000,
            "address", coupon.getId());
        final OrderResponse orderResponse = order(orderRequest).as(OrderResponse.class);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders/" + orderResponse.getId())
            .then()
            .log().all()
            .extract();
        final OrderResponse result = response.as(OrderResponse.class);

        //then
        final List<OrderProductResponse> products = List.of(
            new OrderProductResponse(productId1, "치킨", 10_000, "http://example.com/chicken.jpg", 3),
            new OrderProductResponse(productId1, "치킨", 10_000, "http://example.com/chicken.jpg", 3),
            new OrderProductResponse(productId1, "치킨", 10_000, "http://example.com/chicken.jpg", 3),
            new OrderProductResponse(productId2, "피자", 15_000, "http://example.com/pizza.jpg", 3),
            new OrderProductResponse(productId2, "피자", 15_000, "http://example.com/pizza.jpg", 3),
            new OrderProductResponse(productId2, "피자", 15_000, "http://example.com/pizza.jpg", 3));
        final OrderResponse expectedResponse = new OrderResponse(orderResponse.getId(),
            orderRequest.getTotalProductAmount(), 72_000, orderRequest.getDeliveryAmount(), orderRequest.getAddress(),
            products);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("회원별 주문 목록을 조회한다.")
    void testFindOrderByMember() {
        //given
        final CartItemRequest cartItemRequest1 = new CartItemRequest(productId1, 3);
        final CartItemRequest cartItemRequest2 = new CartItemRequest(productId2, 3);
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemRequest1, cartItemRequest2), 75_000, 3_000,
            "address", coupon.getId());
        final OrderResponse orderResponse = order(orderRequest).as(OrderResponse.class);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders")
            .then()
            .log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
