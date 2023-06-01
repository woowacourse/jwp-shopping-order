package cart.acceptance.coupon;

import static cart.acceptance.common.CommonAcceptanceSteps.given;

import cart.member.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
public class CouponSteps {

    public static ExtractableResponse<Response> 특정_회원의_쿠폰_전체_조회_요청(Member 회원) {
        return given(회원)
                .when().get("/coupons")
                .then().log().all()
                .extract();
    }
}
