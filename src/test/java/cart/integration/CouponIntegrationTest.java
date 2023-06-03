package cart.integration;

import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
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
import static org.hamcrest.Matchers.is;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long amountCouponId;
    private Long rateCouponId;
    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        amountCouponId = couponRepository.insert(AMOUNT_1000_COUPON);
        rateCouponId = couponRepository.insert(RATE_10_COUPON);

        member = memberRepository.getMemberById(1L);
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

    @Test
    @DisplayName("사용자가 쿠폰을 발급받고 조회한다.")
    void issue_coupon_test() {
        // given

        // when
        issueCouponToMember(amountCouponId);

        // then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/coupons/me")
                .then()
                .extract();

        List<CouponResponse> couponResponses = response.body().as(CouponsResponse.class).getCoupons();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(couponResponses).hasSize(1);
        });
    }

    private void issueCouponToMember(long couponId) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(new CouponIssueRequest(couponId))
                .when()
                .post("/coupons/me")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("사용자가 소지하고 있는 쿠폰을 발급 시도한다.")
    void issue_same_coupon_test() {
        // given
        issueCouponToMember(amountCouponId);

        // when
        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(new CouponIssueRequest(amountCouponId))
                .when()
                .post("/coupons/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("이미 가지고 있는 쿠폰입니다."));
    }
}
