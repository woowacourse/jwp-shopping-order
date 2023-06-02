package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.CartItemResponse;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    private Member member1 = new Member(1L, "a@a.com", "1234");
    private Member member2 = new Member(2L, "b@b.com", "1234");

    @Test
    @DisplayName("사용자는 장바구니에 있는 상품을 선택해 주문할 수 있다.")
    void order_success() {
        // given, when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(1L), member1);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderCreateResponse.header("Location")).contains("/orders/1");
    }

    @Test
    @DisplayName("사용자는 전체 주문 내역을 확인할 수 있다.")
    void findAllOrderHistories_success() {
        // given
        createOrder(List.of(1L), member1);
        createOrder(List.of(2L), member1);
        Timestamp time = Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 2, 45, 0));

        // when
        ExtractableResponse<Response> orderHistoryResponse = findOrderHistoryOfMember(member1);

        // then
        assertThat(orderHistoryResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderHistoryResponse.jsonPath().getInt("size()")).isEqualTo(2);
        assertThat(orderHistoryResponse.jsonPath().getObject("[0]", OrderResponse.class))
            .usingRecursiveComparison()
            .ignoringFields("orderDate")
            .isEqualTo(new OrderResponse(1L, BigDecimal.valueOf(20_000), time,
                List.of(
                    new OrderItemResponse(1L, 2, BigDecimal.valueOf(10_000), "치킨", "chickenImg")
                )));
        assertThat(orderHistoryResponse.jsonPath().getObject("[1]", OrderResponse.class))
            .usingRecursiveComparison()
            .ignoringFields("orderDate")
            .isEqualTo(new OrderResponse(2L, BigDecimal.valueOf(80_000), time,
                List.of(
                    new OrderItemResponse(2L, 4, BigDecimal.valueOf(20_000), "샐러드", "saladImg")
                )));
    }

    @Test
    @DisplayName("사용자는 상세 주문 내역을 확인할 수 있다.")
    void findDetailOrderHistory_success() {
        // given
        createOrder(List.of(1L, 2L), member1);

        // when
        ExtractableResponse<Response> detailOrderHistory = findDetailOrderHistory(1L, member1);

        // then
        assertThat(detailOrderHistory.jsonPath().getObject(".", OrderResponse.class))
            .usingRecursiveComparison()
            .ignoringFields("orderDate")
            .isEqualTo(new OrderResponse(1L, BigDecimal.valueOf(100_000),
                Timestamp.valueOf(LocalDateTime.now()),
                List.of(
                    new OrderItemResponse(1L, 2, BigDecimal.valueOf(10_000), "치킨", "chickenImg"),
                    new OrderItemResponse(2L, 4, BigDecimal.valueOf(20_000), "샐러드", "saladImg")
                )));
    }

    @Test
    @DisplayName("자신의 장바구니에 없는 장바구니 상품을 주문하면 주문에 실패한다.")
    void order_noCartItemOfMember_fail() {
        // when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(3L), member1);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(orderCreateResponse.body().jsonPath().getString("message")).
            isEqualTo("해당 장바구니에 접근 권한이 없습니다. cartItemId = 3, memberId = 1");
    }

    @Test
    @DisplayName("존재하지 않는 장바구니 상품을 주문하면 주문에 실패한다.")
    void order_noCartItemId_fail() {
        // when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(4L), member1);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(orderCreateResponse.body().jsonPath().getString("message"))
            .isEqualTo("존재하지 않는 장바구니 상품이 포함되어 있습니다.");
    }

    @Test
    @DisplayName("주문한 장바구니 상품은 장바구니에서 삭제된다.")
    void order_deleteCartItems() {
        // when
        createOrder(List.of(1L), member1);
        ExtractableResponse<Response> cartItemsOfMember = findCartItemsOfMember(member1);

        // then
        List<Long> resultCartItemIds = cartItemsOfMember.jsonPath()
            .getList(".", CartItemResponse.class)
            .stream()
            .map(CartItemResponse::getId)
            .collect(Collectors.toList());
        assertThat(resultCartItemIds).containsExactly(2L);
    }

    private ExtractableResponse<Response> createOrder(List<Long> cartItemIds, Member member) {
        OrderRequest orderRequest = new OrderRequest(cartItemIds);

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

    private ExtractableResponse<Response> findOrderHistoryOfMember(Member member) {
        return given()
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders")
            .then()
            .extract();
    }

    private ExtractableResponse<Response> findDetailOrderHistory(long orderId, Member member) {
        return given()
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .pathParam("id", orderId)
            .when()
            .get("/orders/{id}")
            .then()
            .extract();
    }

    private ExtractableResponse<Response> findCartItemsOfMember(Member member) {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/cart-items")
            .then()
            .log().all()
            .extract();
    }

}
