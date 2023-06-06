package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import cart.ui.dto.request.OrderProductRequest;
import cart.ui.dto.request.OrderRequest;
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

    private Member member;
    private Coupon coupon;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.findById(1L)
            .orElseThrow(RuntimeException::new);

        coupon = couponDao.save(new Coupon("3000원 할인 쿠폰", Amount.of(3_000), Amount.of(10_000), false), member.getId());
    }

    @Test
    @DisplayName("쿠폰을 적용하여 장바구니의 물품들을 주문을 한다.")
    void testOrder() {
        //given
        final OrderProductRequest orderProductRequest1 = new OrderProductRequest(1L, 2);
        final OrderProductRequest orderProductRequest2 = new OrderProductRequest(2L, 4);
        final OrderRequest request = new OrderRequest(List.of(orderProductRequest1, orderProductRequest2), 100_000,
            3_000, "address", coupon.getId());

        //when
        final ExtractableResponse<Response> response = order(request);
        final OrderResponse orderResponse = response.as(OrderResponse.class);

        //then
        final List<OrderProductResponse> products = List.of(
            new OrderProductResponse(1L, "치킨", 10_000,
                "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                2),
            new OrderProductResponse(2L, "샐러드", 20_000,
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                4));
        final OrderResponse expectedResponse = new OrderResponse(orderResponse.getId(), request.getTotalProductAmount(),
            97_000, request.getDeliveryAmount(), request.getAddress(), products);

        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("쿠폰을 적용하지 않고 장바구니의 물품들을 주문을 한다.")
    void testOrderWhenCouponIdIsNull() {
        //given
        final OrderProductRequest orderProductRequest1 = new OrderProductRequest(1L, 2);
        final OrderProductRequest orderProductRequest2 = new OrderProductRequest(2L, 4);
        final OrderRequest request = new OrderRequest(List.of(orderProductRequest1, orderProductRequest2), 100_000,
            3_000, "address", null);

        //when
        final ExtractableResponse<Response> response = order(request);
        final OrderResponse orderResponse = response.as(OrderResponse.class);

        //then
        final List<OrderProductResponse> products = List.of(
            new OrderProductResponse(1L, "치킨", 10_000,
                "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                2),
            new OrderProductResponse(2L, "샐러드", 20_000,
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                4));
        final OrderResponse expectedResponse = new OrderResponse(orderResponse.getId(), request.getTotalProductAmount(),
            100_000, request.getDeliveryAmount(), request.getAddress(), products);

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
        final OrderProductRequest orderProductRequest1 = new OrderProductRequest(1L, 2);
        final OrderProductRequest orderProductRequest2 = new OrderProductRequest(2L, 4);
        final OrderRequest request = new OrderRequest(List.of(orderProductRequest1, orderProductRequest2), 100_000,
            3_000, "address", coupon.getId());
        final OrderResponse orderResponse = order(request).as(OrderResponse.class);

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
            new OrderProductResponse(1L, "치킨", 10_000,
                "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                2),
            new OrderProductResponse(2L, "샐러드", 20_000,
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                4));
        final OrderResponse expectedResponse = new OrderResponse(orderResponse.getId(),
            request.getTotalProductAmount(), 97_000, request.getDeliveryAmount(), request.getAddress(),
            products);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("회원별 주문 목록을 조회한다.")
    void testFindOrderByMember() {
        //given
        final OrderProductRequest orderProductRequest1 = new OrderProductRequest(1L, 2);
        final OrderProductRequest orderProductRequest2 = new OrderProductRequest(2L, 4);
        final OrderRequest request = new OrderRequest(List.of(orderProductRequest1, orderProductRequest2), 100_000,
            3_000, "address", coupon.getId());
        final OrderResponse orderResponse = order(request).as(OrderResponse.class);

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
