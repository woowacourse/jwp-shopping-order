package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.OrderStatus;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import io.restassured.response.*;
import java.util.List;
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
        final ExtractableResponse<Response> response = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE,
                3000L, null));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/orders/1");
    }

    @DisplayName("쿠폰을 적용하여 주문을 추가한다.")
    @Test
    void createOrderUsingCoupon() {
        // given
        final long couponId = 1L;
        // when
        final ExtractableResponse<Response> response = 주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2), DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE,
                3000L, couponId));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/orders/1");
        assertThat(couponRepository.findById(couponId).get().isUsed()).isTrue();
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("주문 정보 목록을 조회한다.")
    @Test
    void showOrders() {
        // given
        final List<Long> cartItemIdsToOrder = List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2);
        주문_정보_추가(member, new OrderRequest(cartItemIdsToOrder, DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_정보_목록_조회(member);
        final List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderResponses).hasSize(1);
        assertThat(orderResponses.get(0).getProducts()).hasSize(cartItemIdsToOrder.size());
    }

    @DisplayName("주문 상세 정보를 조회한다.")
    @Test
    void showOrderDetail() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, 1L));

        // when
        final ExtractableResponse<Response> response = 주문_정보_상세_조회(member, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_삭제(member, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("잘못된 주문 정보로 삭제를 요청하면 실패한다.")
    @Test
    void deleteOrderByIllegalId() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_삭제(member, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 사용자가 주문 삭제를 요청하면 실패한다.")
    @Test
    void deleteOrderByIllegalMemberId() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_삭제(member2, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("주문의 상태를 결제 취소로 변경한다.")
    @Test
    void cancelOrderById() {
        // given
        주문_정보_추가(member, new OrderRequest(
                List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, null));

        // when
        final ExtractableResponse<Response> response = 주문_취소(member, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final ExtractableResponse<Response> result = 주문_정보_목록_조회(member);
        final String orderStatus = result.jsonPath().get("orderStatus").toString();
        assertThat(orderStatus.substring(1, orderStatus.length() - 1))
                .isEqualTo(OrderStatus.CANCEL.getValue());
    }

    @DisplayName("쿠폰을 적용한 주문을 결제 취소하면, 해당 쿠폰은 다시 사용 가능한 상태가 된다.")
    @Test
    void cancelOrderWithCoupon() {
        // given
        final long couponId = 1L;
        주문_정보_추가(member, new OrderRequest(List.of(DUMMY_MEMBER1_CART_ITEM_ID1, DUMMY_MEMBER1_CART_ITEM_ID2),
                DUMMY_MEMBER1_CART_ITEMS_TOTAL_PRICE, 3000L, couponId));

        // when
        final ExtractableResponse<Response> response = 주문_취소(member, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final ExtractableResponse<Response> result = 주문_정보_목록_조회(member);
        final String orderStatus = result.jsonPath().get("orderStatus").toString();
        assertThat(orderStatus.substring(1, orderStatus.length() - 1))
                .isEqualTo(OrderStatus.CANCEL.getValue());
        assertThat(couponRepository.findById(couponId).get().isUsed())
                .isFalse();
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
}
