package com.woowahan.techcourse.coupon.acceptance;

import static com.woowahan.techcourse.common.acceptance.CommonSteps.doGet;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.util.Base64Utils;

@SuppressWarnings("NonAsciiCharacters")
public class CouponStep {

    public static ExtractableResponse<Response> 쿠폰_전체_조회() {
        return doGet("/coupons");
    }

    public static void 쿠폰_사이즈는_N이다(ExtractableResponse<Response> response, int size) {
        assertThat(response.jsonPath().getList("coupons")).hasSize(size);
    }

    public static ExtractableResponse<Response> 멤버의_보유_쿠폰_조회(String email, String password) {
        String header = "Basic " + Base64Utils.encodeToString((email + ":" + password).getBytes());
        return RestAssured.given()
                .log().all()
                .header("Authorization", header)
                .when()
                .get("/members/coupons")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 쿠폰_획득(long 쿠폰_id, String email, String password) {
        String header = "Basic " + Base64Utils.encodeToString((email + ":" + password).getBytes());
        return RestAssured.given()
                .log().all()
                .body("{\"couponId\": " + 쿠폰_id + "}")
                .contentType(ContentType.JSON)
                .header("Authorization", header)
                .when()
                .post("/members/coupon")
                .then()
                .log().all()
                .extract();
    }
}
