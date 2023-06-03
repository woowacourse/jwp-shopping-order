package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.dto.CouponResponse.FixedCouponResponse;
import cart.dto.CouponResponse.RateCouponResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;

public class MemberCouponIntegrationTestFixture {

    public static RateCouponResponse 정률_쿠폰(Long 쿠폰_ID, String 쿠폰_이름, int 할인률, LocalDate 만료기간, int 최소_사용_금액) {
        return new RateCouponResponse(쿠폰_ID, 쿠폰_이름, BigDecimal.valueOf(할인률), 만료기간, BigDecimal.valueOf(최소_사용_금액));
    }

    public static FixedCouponResponse 정액_쿠폰(Long 쿠폰_ID, String 쿠폰_이름, int 할인금액, LocalDate 만료기간, int 최소_사용_금액) {
        return new FixedCouponResponse(쿠폰_ID, 쿠폰_이름, BigDecimal.valueOf(할인금액), 만료기간, BigDecimal.valueOf(최소_사용_금액));
    }

    public static CouponResponse 쿠폰_전체_조회_응답(List<RateCouponResponse> 정률_쿠폰들, List<FixedCouponResponse> 정액_쿠폰들) {
        return new CouponResponse(정률_쿠폰들, 정액_쿠폰들);
    }

    public static void 쿠폰_전체_조회_응답_검증(ExtractableResponse<Response> 응답, CouponResponse couponResponse) {
        assertThat(응답.as(CouponResponse.class)).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .withEqualsForType((d1, d2) -> d1.compareTo(d2) == 0, BigDecimal.class)
                .isEqualTo(couponResponse);
    }

    public static ExtractableResponse<Response> 쿠폰_조회_요청(Member 사용자) {
        return RestAssured.given()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/coupons")
                .then()
                .extract();
    }
}
