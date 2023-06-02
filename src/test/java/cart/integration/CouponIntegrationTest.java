package cart.integration;

import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.repository.CouponRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private CouponRepository couponRepository;

    private Long amountCouponId;
    private Long rateCouponId;

    @BeforeEach
    void setUp() {
        super.setUp();

        amountCouponId = couponRepository.insert(AMOUNT_1000_COUPON);
        rateCouponId = couponRepository.insert(RATE_10_COUPON);
    }

    @Test
    @DisplayName("모든 쿠폰을 조회한다")
    void get_all_coupons_test() {
        // given

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();

        // then
        List<CouponResponse> couponResponses = response.body().as(CouponsResponse.class).getCoupons();
        CouponResponse actualRateCouponResponse = couponResponses.stream()
                .filter(couponResponse -> couponResponse.getId().equals(rateCouponId))
                .findFirst().get();
        CouponResponse actualAmountCouponResponse = couponResponses.stream()
                .filter(couponResponse -> couponResponse.getId().equals(amountCouponId))
                .findFirst().get();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(couponResponses).hasSize(2);
            softly.assertThat(actualRateCouponResponse.getType()).isEqualTo("percent");
            softly.assertThat(actualRateCouponResponse.getAmount()).isEqualTo(10);
            softly.assertThat(actualAmountCouponResponse.getType()).isEqualTo("amount");
            softly.assertThat(actualAmountCouponResponse.getAmount()).isEqualTo(1000);
        });
    }
}
