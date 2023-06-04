package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderItemsResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberIntegrationTest extends IntegrationTest {

    @DisplayName("초기 회원을 생성하면 기본 포인트는 5,000원이다.")
    @Test
    void member_initial_point() {
        Member member = memberTestSupport.builder().build();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("members/points")
                .then()
                .extract();
        int point = response.jsonPath().getInt("point");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(point).isEqualTo(5_000)
        );
    }

    @DisplayName("회원의 전체 주문 내역을 조회한다.")
    @Test
    void showOrders() {
        Member member = memberTestSupport.builder().build();
        OrderHistory orderHistory1 = orderHistoryTestSupport.builder().member(member).build();
        OrderItem orderItem1 = orderItemTestSupport.builder().orderHistory(orderHistory1).build();
        OrderItem orderItem2 = orderItemTestSupport.builder().orderHistory(orderHistory1).build();
        OrderHistory orderHistory2 = orderHistoryTestSupport.builder().member(member).build();
        OrderItem orderItem3 = orderItemTestSupport.builder().orderHistory(orderHistory2).build();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/orders")
                .then()
                .extract();

        OrderItemsResponse orderItemsResponse = response.as(OrderItemsResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderItemsResponse.getOrderItems().size()).isEqualTo(2),
                () -> assertThat(orderItemsResponse.getOrderItems().get(0).getOrderId()).isEqualTo(
                        orderHistory1.getId()),
                () -> assertThat(orderItemsResponse.getOrderItems().get(0).getPreviewName()).isEqualTo(
                        orderItem1.getName()),
                () -> assertThat(orderItemsResponse.getOrderItems().get(1).getOrderId()).isEqualTo(
                        orderHistory2.getId()),
                () -> assertThat(orderItemsResponse.getOrderItems().get(1).getPreviewName()).isEqualTo(
                        orderItem3.getName())
        );
    }

    @DisplayName("회원의 주문 내역을 상세 조회한다.")
    @Test
    void showOrderDetail() {
        Member member = memberTestSupport.builder().build();
        OrderHistory orderHistory = orderHistoryTestSupport.builder().member(member).build();
        OrderItem orderItem1 = orderItemTestSupport.builder().orderHistory(orderHistory).build();
        OrderItem orderItem2 = orderItemTestSupport.builder().orderHistory(orderHistory).build();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("members/orders/{orderId}", orderHistory.getId())
                .then()
                .extract();

        OrderDetailResponse orderDetailResponse = response.as(OrderDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponse.getOrderItems().size()).isEqualTo(2),
                () -> assertThat(orderDetailResponse.getOrderPrice()).isEqualTo(orderHistory.getOrderPrice()),
                () -> assertThat(orderDetailResponse.getOriginalPrice()).isEqualTo(orderHistory.getOriginalPrice()),
                () -> assertThat(orderDetailResponse.getUsedPoints()).isEqualTo(orderHistory.getUsedPoint())
        );
    }
}
