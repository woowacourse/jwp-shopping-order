package cart.integration;

import cart.dao.MemberDao;
import cart.dao.coupon.CouponDao;
import cart.dto.order.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("장바구니에 있는 상품에 쿠폰을 적용한다.")
    void apply() {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/cart-items/coupons?id=1")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("장바구니에 있는 상품에 중복 쿠폰을 적용하는 경우 에러가 발생한다.")
    void apply1() {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/cart-items/coupons?id=2,3")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("장바구니에 있는 상품을 결제한다.")
    void order() {
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), List.of(1L), 23000, false);
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .post("/payments")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("장바구니에 있는 상품을 결제하는데 가격이 일치 하지 않는 경우 에러가 발생한다.")
    void order1() {
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), List.of(1L), 2000, false);
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .post("/payments")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("주문 내역을 가져온다.")
    void getHistory() {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/orders")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("상세 주문 내역을 가져온다.")
    void getDetailHistory() {
        OrderRequest orderRequest = new OrderRequest(List.of(3L), List.of(), 13000, false);
        String location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .post("/payments")
                .then()
                .extract().header("Location");

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get(location)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }



}
