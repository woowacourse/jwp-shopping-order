package cart.integration;


import static cart.fixture.CouponFixture.십프로_할인_쿠폰;
import static cart.fixture.CouponFixture.천원_할인_쿠폰;
import static cart.fixture.JdbcTemplateFixture.insertCoupon;
import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertMemberCoupon;
import static cart.fixture.MemberFixture.MEMBER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.response.MemberCouponResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponController 통합테스트 은(는)")
public class CouponIntegrationTest extends IntegrationTest {

    private static final String API_URL = "/coupons";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 특정한_회원의_모든_쿠폰을_조회한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        insertCoupon(십프로_할인_쿠폰, jdbcTemplate);
        insertMemberCoupon(MEMBER, 천원_할인_쿠폰, jdbcTemplate);
        insertMemberCoupon(MEMBER, 십프로_할인_쿠폰, jdbcTemplate);

        // when
        ExtractableResponse<Response> response = 회원의_쿠폰을_조회한다(MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MemberCouponResponse> body = response.as(new ParameterizedTypeReference<List<MemberCouponResponse>>() {
                }.getType()
        );
        쿠폰을_검증한다(body.get(0), 천원_할인_쿠폰);
        쿠폰을_검증한다(body.get(1), 십프로_할인_쿠폰);
    }

    private static ExtractableResponse<Response> 회원의_쿠폰을_조회한다(Member 회원) {
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();
    }

    private void 쿠폰을_검증한다(MemberCouponResponse 결과, Coupon 쿠폰) {
        assertAll(
                () -> assertThat(결과.getId()).isEqualTo(쿠폰.getId()),
                () -> assertThat(결과.getName()).isEqualTo(쿠폰.getName()),
                () -> assertThat(결과.getDiscount().getType()).isEqualTo(쿠폰.getType().name()),
                () -> assertThat(결과.getDiscount().getAmount()).isEqualTo(쿠폰.getDiscountAmount())
        );
    }
}
