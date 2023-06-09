package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.dto.request.OrderRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderIntegrationTest extends IntegrationTest {

    private Member member1 = new Member(1L, new Email("a@a.com"), new Password("1234"));
    private final BigDecimal pointToUseNotUsed = BigDecimal.valueOf(0);

    @Test
    void 사용자는_장바구니에_있는_상품을_선택해_주문할_수_있다() {
        // given, when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(1L), member1, pointToUseNotUsed);

        // then
        assertAll(
                () -> assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderCreateResponse.header("Location")).contains("/orders/1")
        );


    }

    @Test
    void 사용자는_전체_주문_내역을_확인할_수_있다() {
        // given
        createOrder(List.of(1L), member1, pointToUseNotUsed);
        createOrder(List.of(2L), member1, pointToUseNotUsed);
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
                                new OrderItemResponse(1L, 2, BigDecimal.valueOf(10_000), "치킨",
                                        "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
                        )));
        assertThat(orderHistoryResponse.jsonPath().getObject("[1]", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringFields("orderDate", "orders[0].imageUrl")
                .isEqualTo(new OrderResponse(2L, BigDecimal.valueOf(80_000), time,
                        List.of(
                                new OrderItemResponse(2L, 4, BigDecimal.valueOf(20_000), "샐러드",
                                        "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
                        )));
    }

    @Test
    void 사용자는_상세_주문_내역을_확인할_수_있다() {
        // given
        createOrder(List.of(1L, 2L), member1, pointToUseNotUsed);

        // when
        ExtractableResponse<Response> detailOrderHistory = findDetailOrderHistory(1L, member1);

        // then
        assertThat(detailOrderHistory.jsonPath().getObject(".", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringFields("orderDate")
                .ignoringFields("imageUrl")
                .isEqualTo(new OrderResponse(1L, BigDecimal.valueOf(100_000),
                        Timestamp.valueOf(LocalDateTime.now()),
                        List.of(
                                new OrderItemResponse(1L, 2, BigDecimal.valueOf(10_000), "치킨",
                                        "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                                new OrderItemResponse(2L, 4, BigDecimal.valueOf(20_000), "샐러드",
                                        "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
                        )));
    }

    @Test
    void 자신의_장바구니에_없는_장바구니_상품을_주문하면_주문에_실패한다() {
        // when
        ExtractableResponse<Response> orderCreateResponse = createOrder(List.of(3L), member1, pointToUseNotUsed);

        // then
        assertThat(orderCreateResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 주문한_장바구니_상품은_장바구니에서_삭제된다() {
        // when
        createOrder(List.of(1L), member1, pointToUseNotUsed);
        ExtractableResponse<Response> cartItemsOfMember = findCartItemsOfMember(member1);

        // then
        List<Long> resultCartItemIds = cartItemsOfMember.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        Assertions.assertThat(resultCartItemIds).containsExactly(2L);
    }

    private ExtractableResponse<Response> createOrder(List<Long> cartItemIds, Member member, BigDecimal pointToUse) {
        OrderRequest orderRequest = new OrderRequest(cartItemIds, pointToUse);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> findOrderHistoryOfMember(Member member) {
        return given()
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/orders")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> findDetailOrderHistory(Long orderId, Member member) {
        return given()
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .pathParam("id", orderId)
                .when()
                .get("/orders/{id}")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> findCartItemsOfMember(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail().email(), member.getPassword().password())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }
}
