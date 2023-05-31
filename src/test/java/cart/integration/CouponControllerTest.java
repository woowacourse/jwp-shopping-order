package cart.integration;

import cart.dto.coupon.CouponRequest;
import cart.dto.coupon.CouponResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class CouponControllerTest extends IntegrationTest {

    @Test
    void adminShowCoupons() {
        ExtractableResponse<Response> result = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/admin/coupons")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void adminDeleteCoupons() {
        CouponRequest couponRequest = new CouponRequest("오픈 기념 쿠폰", "rate", 10);
        ExtractableResponse<Response> createResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponRequest)
                .when()
                .post("/admin/coupon")
                .then()
                .extract();

        long couponId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> response = RestAssured.given()
                .body(couponRequest)
                .when()
                .delete("/admin/coupon/{couponId}", couponId)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void memberAddCoupon() {
        ExtractableResponse<Response> createResponse = RestAssured.given()
                .auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .body(1L)
                .when()
                .post("/coupon")
                .then().log().all()
                .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void memberShowCoupons() {
        List<CouponResponse> responses = RestAssured.given()
                .auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .when()
                .get("/coupons")
                .then().log().all()
                .extract()
                .response()
                .jsonPath().getList(".", CouponResponse.class);

        assertThat(responses).doesNotContainNull();
    }
}
