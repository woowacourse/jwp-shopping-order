package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.request.CartItemIdRequest;
import cart.dto.request.PaymentRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PayIntegrationTest extends IntegrationTest {

    @DisplayName("결제하려는 상품이 없을 때 Bad Request를 응답한다.")
    @Test
    void pay_fail_no_cartItems() {
        PaymentRequest paymentRequest = new PaymentRequest(null, 1_000, 100);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paymentRequest)
                .when()
                .post("/pay")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상품 금액의 합이 0보다 작은 경우 Bad Request를 응답한다.")
    @Test
    void pay_fail_negative_price() {
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(1L)), -1, 100);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paymentRequest)
                .when()
                .post("/pay")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("포인트가 0보다 작은 경우 Bad Request를 응답한다.")
    @Test
    void pay_fail_negative_point() {
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(1L)), 1_000, -1);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paymentRequest)
                .when()
                .post("/pay")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("결제에 성공하면 생성된 주문의 ID와 OK를 반환한다.")
    @Test
    void pay_success() {
        Member member = memberTestSupport.builder().build();
        CartItem cartItem = cartItemTestSupport.builder().member(member).build();
        int originalPrice = cartItem.getQuantity() * cartItem.getProduct().getPrice();
        int usedPoints = 100;
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(cartItem.getId())),
                originalPrice, usedPoints);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paymentRequest)
                .when().log().all()
                .post("/pay")
                .then().log().all().extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getLong("orderId")).isPositive()
        );
    }

    @DisplayName("결제 후 회원의 포인트는 사용한 만큼 차감되고 실제 주문 금액의 5%만큼 적립된다.")
    @Test
    void pay_success_point() {
        Member member = memberTestSupport.builder().build();
        int previousPoint = member.getPointAsInt();
        CartItem cartItem = cartItemTestSupport.builder().member(member).build();
        int originalPrice = cartItem.getQuantity() * cartItem.getProduct().getPrice();
        int usedPoints = 100;
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(cartItem.getId())),
                originalPrice, usedPoints);

        RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paymentRequest)
                .when()
                .post("/pay");

        int currentPoint = RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/points")
                .jsonPath().getInt("point");

        int expectedPoint = previousPoint - usedPoints + (int) Math.ceil((originalPrice - usedPoints) * 0.05);
        assertThat(currentPoint).isEqualTo(expectedPoint);
    }
}
