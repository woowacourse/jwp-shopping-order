package cart.integration;

import cart.dto.coupon.CouponRequest;
import cart.dto.coupon.CouponResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class CouponControllerTest extends IntegrationTest {

    @Test
    void showAllCoupons() {
        ExtractableResponse<Response> result = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/coupons")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void showCoupons() {
        CouponRequest couponRequest = new CouponRequest("오픈 기념 쿠폰", "rate", 10);
        ExtractableResponse<Response> createResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when()
                .post("/coupon")
                .then()
                .extract();

        long couponId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        CouponResponse couponResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/coupon/{couponId}", couponId)
                .then()
                .extract()
                .jsonPath()
                .getObject(".", CouponResponse.class);

        Assertions.assertAll(
                () -> assertThat(couponRequest.getName()).isEqualTo(couponResponse.getName()),
                () -> assertThat(couponRequest.getType()).isEqualTo(couponResponse.getDiscount().getType()),
                () -> assertThat(couponRequest.getAmount()).isEqualTo(couponResponse.getDiscount().getAmount())
        );
    }

    @Test
    void addCoupon() {
        CouponRequest couponRequest = new CouponRequest("오픈 기념 쿠폰", "rate", 10);
        ExtractableResponse<Response> createResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when()
                .post("/coupon")
                .then().log().all()
                .extract();

        long couponId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        assertThat(couponId).isEqualTo(1L);
    }

    @Test
    void deleteCoupons() {
        CouponRequest couponRequest = new CouponRequest("오픈 기념 쿠폰", "rate", 10);
        ExtractableResponse<Response> createResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when()
                .post("/coupon")
                .then()
                .extract();

        long couponId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> response = RestAssured.given()
                .body(couponRequest)
                .when()
                .delete("/coupon/{couponId}", couponId)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
