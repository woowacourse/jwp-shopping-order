package com.woowahan.techcourse.coupon.acceptance;

import static com.woowahan.techcourse.common.acceptance.CommonSteps.doGet;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
public class CouponStep {

    public static ExtractableResponse<Response> 쿠폰_전체_조회() {
        return doGet("/coupons");
    }

    public static ExtractableResponse<Response> 쿠폰을_멤버_ID_로_조회(Long id) {
        return doGet("/members/coupons");
    }

    public static void 쿠폰_사이즈는_N이다(ExtractableResponse<Response> response, int size) {
        assertThat(response.jsonPath().getList("coupons").size()).isEqualTo(size);
    }
}
