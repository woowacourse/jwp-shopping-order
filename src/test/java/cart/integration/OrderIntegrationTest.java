package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.OrderStatus;
import cart.dto.CouponResponse;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import io.restassured.response.*;
import java.util.List;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    private static final Long DUMMY_MEMBER1_CART_ITEM_ID1 = 1L;
    private static final Long DUMMY_MEMBER1_CART_ITEM_ID2 = 2L;
    private static final Long DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE = 380_400L;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CouponRepository couponRepository;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberRepository.findById(1L).get();
        member2 = memberRepository.findById(2L).get();
    }

    @DisplayName("쿠폰 없이 주문을 추가한다.")
    @Test
    void createOrder() {
        // given, when
        final long deliveryFee = 3000L;
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE,
                deliveryFee, null));

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createdResponse.header("Location")).isEqualTo("/orders/1");

        final ExtractableResponse<Response> foundResponse = 주문_정보_상세_조회(member,
                getIdFromCreatedResponse(createdResponse));
        final OrderDetailResponse order = foundResponse.body().jsonPath().getObject(".", OrderDetailResponse.class);
        assertThat(order.getTotalPrice()).isEqualTo(DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE);
        assertThat(order.getDeliveryFee()).isEqualTo(deliveryFee);
        assertThat(order.getCoupon()).isNull();
    }

    @DisplayName("쿠폰을 적용하여 주문을 추가한다.")
    @Test
    void createOrderUsingCoupon() {
        // given
        final long couponId = 1L;
        final long deliveryFee = 3000L;
        // when
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE,
                deliveryFee, couponId));

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createdResponse.header("Location")).isEqualTo("/orders/1");

        final ExtractableResponse<Response> foundResponse = 주문_정보_상세_조회(member,
                getIdFromCreatedResponse(createdResponse));
        final OrderDetailResponse order = foundResponse.body().jsonPath().getObject(".", OrderDetailResponse.class);
        assertThat(order.getTotalPrice()).isEqualTo(DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE);
        assertThat(order.getDeliveryFee()).isEqualTo(deliveryFee);
        assertThat(order.getCoupon()).isNotNull();
        assertThat(order.getCoupon().getId()).isEqualTo(couponId);
    }

    @DisplayName("잘못된 사용자 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addOrderByIllegalMember() {
        // given
        final Member illegalMember = new Member(member.getId(), member.getEmail(),
                member.getPassword() + "asdf");

        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(illegalMember, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("다른 사용자의 장바구니 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addOrderByIllegalCartItem() {
        // given, when
        final ExtractableResponse<Response> response = 주문_정보_추가(member2, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE,
                3000L, null));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 장바구니 아이템 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addNotExistingCartItem() {
        // given
        final List<Long> cartItemIds = List.of(-1L);

        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(member, new OrderRequest(
                cartItemIds, DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 총 금액으로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addByIllegalTotalPrice() {
        // given
        final long totalPrice = 4000L;

        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), totalPrice, 3000L, null));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("잘못된 쿠폰 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addByNotExistingCoupon() {
        // given
        final long couponId = 10L;

        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, couponId));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사용자의 쿠폰 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addByIllegalCoupon() {
        // given
        final long couponId = 1L;

        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(member2, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, couponId));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문 정보 목록을 조회한다.")
    @Test
    void showOrders() {
        // given
        final List<CouponResponse> coupons = 쿠폰_목록_조회(member).jsonPath().getList(".", CouponResponse.class);
        final CouponResponse coupon = coupons.get(0);
        final List<Long> cartItemIdsToOrder = List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2);
        final long discountPrice = 3000L;
        주문_정보_추가(member, new OrderRequest(cartItemIdsToOrder, DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, discountPrice,
                coupon.getId()));

        // when
        final ExtractableResponse<Response> response = 주문_정보_목록_조회(member);
        final List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderResponses).hasSize(1);
        final OrderResponse orderResponse = orderResponses.get(0);
        assertThat(orderResponse.getOrderStatus()).isEqualTo("결제완료");
        assertThat(orderResponse.getTotalPayments()).isEqualTo(
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE + discountPrice - coupon.getPriceDiscount());
    }

    @DisplayName("주문 상세 정보를 조회한다.")
    @Test
    void showOrderDetail() {
        // given
        final List<CouponResponse> coupons = 쿠폰_목록_조회(member).jsonPath().getList(".", CouponResponse.class);
        final long couponId = coupons.get(0).getId();
        final long deliveryFee = 3000L;
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, couponId));

        // when
        final ExtractableResponse<Response> response = 주문_정보_상세_조회(member, getIdFromCreatedResponse(createdResponse));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final OrderDetailResponse order = response.body().jsonPath().getObject(".", OrderDetailResponse.class);
        assertThat(order.getTotalPrice()).isEqualTo(DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE);
        assertThat(order.getDeliveryFee()).isEqualTo(deliveryFee);
        assertThat(order.getCoupon()).isNotNull();
        assertThat(order.getCoupon().getId()).isEqualTo(couponId);
        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE.getValue());
    }

    @DisplayName("잘못된 아이디의 주문 상세 정보 조회 요청 시 실패한다.")
    @Test
    void showOrderDetailByIllegalId() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_정보_상세_조회(member, -1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문을 삭제한다.")
    @Test
    void deleteOrder() {
        // given
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final long orderId = getIdFromCreatedResponse(createdResponse);
        final ExtractableResponse<Response> response = 주문_삭제(member, orderId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(주문_정보_상세_조회(member, orderId).statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 주문 정보로 삭제를 요청하면 실패한다.")
    @Test
    void deleteOrderByIllegalId() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_삭제(member, -1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 사용자가 주문 삭제를 요청하면 실패한다.")
    @Test
    void deleteOrderByIllegalMemberId() {
        // given
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_삭제(member2, getIdFromCreatedResponse(createdResponse));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문의 상태를 결제 취소로 변경한다.")
    @Test
    void cancelOrderById() {
        // given
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final long orderId = getIdFromCreatedResponse(createdResponse);
        final ExtractableResponse<Response> response = 주문_취소(member, orderId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final ExtractableResponse<Response> foundResponse = 주문_정보_목록_조회(member);
        final List<OrderResponse> orderResponses = foundResponse.jsonPath().getList(".", OrderResponse.class);
        System.out.println(orderResponses);
        orderResponses.stream()
                .filter(orderResponse -> Objects.equals(orderResponse.getOrderId(), orderId))
                .findFirst()
                .ifPresentOrElse(
                        order -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL.getValue()),
                        () -> Assertions.fail("order not exist; orderId=" + orderId)
                );
    }

    @DisplayName("쿠폰을 적용한 주문을 결제 취소하면, 해당 쿠폰은 다시 사용 가능한 상태가 된다.")
    @Test
    void cancelOrderWithCoupon() {
        // given
        final long couponId = 1L;
        final ExtractableResponse<Response> createdResponse = 주문_정보_추가(member,
                new OrderRequest(List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                        DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, couponId));

        // when
        final ExtractableResponse<Response> response = 주문_취소(member, getIdFromCreatedResponse(createdResponse));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final ExtractableResponse<Response> result = 주문_정보_목록_조회(member);
        final String orderStatus = result.jsonPath().get("orderStatus").toString();
        assertThat(orderStatus.substring(1, orderStatus.length() - 1))
                .isEqualTo(OrderStatus.CANCEL.getValue());
        assertThat(couponRepository.findByIdForMember(member.getId(), couponId).get().isUsed())
                .isFalse();
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> 주문_정보_추가(final Member member, final OrderRequest orderRequest) {
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

    private ExtractableResponse<Response> 주문_정보_목록_조회(final Member member) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_정보_상세_조회(final Member member, final Long orderId) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_삭제(final Member member, final Long orderId) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_취소(final Member member, final Long orderId) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .patch("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿠폰_목록_조회(final Member member) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();
    }
}
