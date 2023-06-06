package cart.integration;

import cart.domain.member.Member;
import cart.dto.MemberCouponsResponse;
import cart.dto.MemberCouponsResponse.FixedCouponResponse;
import cart.dto.MemberCouponsResponse.RateCouponResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberCouponIntegrationTestFixture {

    public static RateCouponResponse 정률_쿠폰_정보를_반환한다(Long 쿠폰_ID, String 쿠폰_이름, int 할인률, LocalDate 만료기간, int 최소_사용_금액) {
        return new RateCouponResponse(쿠폰_ID, 쿠폰_이름, BigDecimal.valueOf(할인률), 만료기간, BigDecimal.valueOf(최소_사용_금액));
    }

    public static FixedCouponResponse 정액_쿠폰_정보를_반환한다(Long 쿠폰_ID, String 쿠폰_이름, int 할인금액, LocalDate 만료기간, int 최소_사용_금액) {
        return new FixedCouponResponse(쿠폰_ID, 쿠폰_이름, BigDecimal.valueOf(할인금액), 만료기간, BigDecimal.valueOf(최소_사용_금액));
    }

    public static MemberCouponsResponse 쿠폰_전체_조회_정보를_반환한다(List<RateCouponResponse> 정률_쿠폰들, List<FixedCouponResponse> 정액_쿠폰들) {
        return new MemberCouponsResponse(정률_쿠폰들, 정액_쿠폰들);
    }

    public static void 쿠폰_전체_조회_응답을_검증한다(ExtractableResponse<Response> 응답, MemberCouponsResponse couponsResponse) {
        assertThat(응답.as(MemberCouponsResponse.class)).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .withEqualsForType((d1, d2) -> d1.compareTo(d2) == 0, BigDecimal.class)
                .isEqualTo(couponsResponse);
    }

    public static ExtractableResponse<Response> 쿠폰_조회를_요청한다(Member 사용자) {
        return RestAssured.given()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/coupons")
                .then()
                .extract();
    }
}
