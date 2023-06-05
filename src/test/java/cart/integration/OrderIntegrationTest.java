package cart.integration;

import cart.domain.Member.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderItemResponse;
import cart.dto.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static cart.integration.apifixture.ApiFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderIntegrationTest extends IntegrationTest {
    private final Member member1 = new Member(1L, "a@a.com", "1234");
    private final Member member2 = new Member(2L, "b@b.com", "1234");
    private final Long productId1 = 1L;
    private final Long productId2 = 2L;


    @Test
    @DisplayName("사용자는 장바구니에 있는 상품을 선택해 주문할 수 있다.")
    void order_success() {
        // given, when
        long cartId = addCartItemAndGetId(member1, new CartItemRequest(productId1));
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(cartId), member1, 0);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderCreateResponse.header("Location")).contains("/orders/1");
    }

    @Test
    @DisplayName("사용자는 전체 주문 내역을 확인할 수 있다.")
    void findAllOrderHistories_success() {
        // given
        long cartId1 = addCartItemAndGetId(member1, new CartItemRequest(productId1));
        long cartId2 = addCartItemAndGetId(member1, new CartItemRequest(productId2));

        requestUpdateCartItemQuantity(member1, cartId1, 5);

        createOrder(List.of(cartId1), member1, 0);
        createOrder(List.of(cartId2), member1, 0);

        Timestamp time = Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 2, 45, 00));

        // when
        ExtractableResponse<Response> orderHistoryResponse = showUserOrderHistory(member1);

        // then
        assertThat(orderHistoryResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderHistoryResponse.jsonPath().getInt("size()")).isEqualTo(2);
        assertThat(orderHistoryResponse.jsonPath().getObject("[0]", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringFields("orderDate")
                .isEqualTo(new OrderResponse(1L, 50_000, time,
                        List.of(
                                new OrderItemResponse(1L, 5, "치킨", 10_000, "https://images.sample")
                        )));
        assertThat(orderHistoryResponse.jsonPath().getObject("[1]", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringFields("orderDate")
                .isEqualTo(new OrderResponse(2L, 20_000, time,
                        List.of(
                                new OrderItemResponse(2L, 1, "샐러드", 20_000, "https://images.sample")
                        )));
    }

    @Test
    @DisplayName("사용자의 주문 내역이 존재하지 않아도 주문 내역을 조회할 수 있다.")
    void findAllOrderHistories_WhenWithNoOrder() {
        // when
        ExtractableResponse<Response> orderHistoryResponse = showUserOrderHistory(member1);

        // then
        assertThat(orderHistoryResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderHistoryResponse.body().as(List.class).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자는 상세 주문 내역을 확인할 수 있다.")
    void findDetailOrderHistory_success() {
        long cartId1 = addCartItemAndGetId(member1, new CartItemRequest(productId1));
        long cartId2 = addCartItemAndGetId(member1, new CartItemRequest(productId2));

        requestUpdateCartItemQuantity(member1, cartId1, 2);
        requestUpdateCartItemQuantity(member1, cartId2, 4);

        // given
        long orderId = addOrderAndGetId(List.of(cartId1, cartId2), member1, 0);

        // when
        ExtractableResponse<Response> detailOrderHistory = showOrderDetailHistory(orderId, member1);

        // then
        assertThat(detailOrderHistory.jsonPath().getObject(".", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringFields("orderDate")
                .isEqualTo(new OrderResponse(1L, 100_000, Timestamp.valueOf(LocalDateTime.now()),
                        List.of(
                                new OrderItemResponse(1L, 2, "치킨", 10_000, "https://images.sample"),
                                new OrderItemResponse(2L, 4, "샐러드", 20_000, "https://images.sample")
                        )));
    }

    @Test
    @DisplayName("자신의 장바구니에 없는 장바구니 상품을 주문하면 주문에 실패한다.")
    void order_otherMembersCartItem_fail() {
        // when
        long cartId = addCartItemAndGetId(member2, new CartItemRequest(productId1));
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(cartId), member1, 0);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("장바구니에 없는 상품을 주문하면 실패한다.")
    void order_noCartItemFound_fail() {
        // when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(3L), member1, 0);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("다른 유저의 주문을 조회하면 실패한다.")
    void findOrder_otherMembersOrder_fail() {
        // when
        long member1CartId = addCartItemAndGetId(member1, new CartItemRequest(productId1));
        long member1OrderId = addOrderAndGetId(List.of(member1CartId), member1, 0);

        ExtractableResponse<Response> response = showOrderDetailHistory(member1OrderId, member2);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

}

