package cart.integration;

import cart.dto.CouponRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cart.fixture.Fixture.EMAIL;
import static cart.fixture.Fixture.PASSWORD;

public class CouponIntegrationTest extends IntegrationTest {
    @Test
    @DisplayName("유저에게 쿠폰을 발급한다.")
    void issueCouponTest() {
        final CouponRequest couponRequest = new CouponRequest(1L);
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when().post("/users/coupons")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    // TODO : coupons 변수명 확정하기
    @Test
    @DisplayName("모든 쿠폰 조회")
    void getAllCouponsTest() {
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().get("/coupons")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("유저 쿠폰 조회")
    void getUserCouponsTest() {
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().get("/users/coupons")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
