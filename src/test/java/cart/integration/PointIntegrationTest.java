package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PointIntegrationTest extends IntegrationTest {

    private final Member member = new Member(1L, "a@a.com", "1234");

    @Test
    @DisplayName("주문을 한 후, 총 결제 금액의 2.5%의 적립된 포인트가 적립된다.")
    void order_savedPoint_success() {
        // given
        ExtractableResponse<Response> orderResponse = createOrder(List.of(1L, 2L), member, 1000);
        String location = orderResponse.header("Location");
        long orderId = Long.parseLong(location.replace("/orders/", ""));

        // when
        ExtractableResponse<Response> savedPoint = findSavedPoint(orderId);

        // then
        assertThat(savedPoint.body().jsonPath().getInt("pointsSaved"))
            .isEqualTo(2475);
    }

    @Test
    @DisplayName("주문 시 사용할 포인트가 상품 금액보다 크면 주문에 실패한다.")
    void order_usePoint_overPrice_fail() {
        // given
        Member memberWith100000Point = new Member(3L, "b@b.com", "1234");

        // when
        ExtractableResponse<Response> response = createOrder(List.of(3L), memberWith100000Point, 65001);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("message"))
            .isEqualTo("상품 금액보다 큰 포인트는 사용할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 시 사용할 포인트가 0보다 작다면 주문에 실패한다.")
    void order_usePoint_lessThan0_fail() {
        // when
        ExtractableResponse<Response> response = createOrder(List.of(1L), member, -1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("message"))
            .isEqualTo("포인트는 0보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 시 사용할 포인트가 보유 포인트보다 크다면 주문에 실패한다.")
    void order_usePoint_moreThanMemberPoint_fail() {
        // when
        ExtractableResponse<Response> response = createOrder(List.of(1L), member, 1001);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("message"))
            .isEqualTo("보유한 포인트보다 많은 포인트를 사용할 수 없습니다");
    }

    @Test
    @DisplayName("주문 후 총 잔여 포인트를 확인할 수 있다.")
    void order_leftPoint_success() {
        // given
        createOrder(List.of(1L, 2L), member, 500);

        // when 2488
        ExtractableResponse<Response> pointOfMember = findPointOfMember(member);

        // then
        assertThat(pointOfMember.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(pointOfMember.body().jsonPath().getInt("points"))
            .isEqualTo(2988);
    }

    private ExtractableResponse<Response> createOrder(List<Long> cartItemIds, Member member, int pointToUse) {
        OrderRequest orderRequest = new OrderRequest(cartItemIds, pointToUse);

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

    private ExtractableResponse<Response> findSavedPoint(long orderId) {
        return given().log().all()
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders/{id}/points", orderId)
            .then()
            .log().all()
            .extract();
    }

    private ExtractableResponse<Response> findPointOfMember(Member member) {
        return given().log().all()
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/points")
            .then()
            .log().all()
            .extract();
    }

}
